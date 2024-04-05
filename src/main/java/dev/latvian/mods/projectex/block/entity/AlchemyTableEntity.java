package dev.latvian.mods.projectex.block.entity;

import dev.latvian.mods.projectex.ProjectEX;
import dev.latvian.mods.projectex.inventor.BaseItemStackHandler;
import dev.latvian.mods.projectex.menu.AlchemyTableMenu;
import dev.latvian.mods.projectex.recipes.AlchemyTableRecipe;
import dev.latvian.mods.projectex.recipes.ModRecipeTypes;
import dev.latvian.mods.projectex.recipes.RecipeCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AlchemyTableEntity extends AbstractEMCBlockEntity implements MenuProvider {

	public int progress = 0;

	public long totalCost = 0L;
	public int craftingTime = 0;

	public final TrackedData trackedData = new TrackedData();
	// client-side, synced via TrackedData
	public int progressDisplay;
	public int costDisplay;

	private final ItemStackHandler inventory = new BaseItemStackHandler(this, 2) {
		@Override
		public boolean isItemValid(int slot, @NotNull ItemStack stack) {
			ProjectEX.LOGGER.info("isItemValid: " + stack.getItem().getRegistryName());
			return slot == 0 && findRecipeFor(stack).isPresent();
		}
	};
	private final IItemHandler wrapped = new WrappedAlchemyHandler(inventory);
	private final LazyOptional<IItemHandler> itemCap = LazyOptional.of(() -> wrapped);

	public AlchemyTableEntity() {
		super(ProjectEXBlockEntities.ALCHEMY_TABLE.get());
	}


	@Override
	public Component getDisplayName() {
		return new TranslatableComponent("container.projectex.alchemy_table");
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int i, Inventory arg, Player arg2) {
		return new AlchemyTableMenu(i, arg, getBlockPos());
	}
//	public AlchemyTableEntity() {
//		super(ProjectEXBlockEntities.ALCHEMY_TABLE.get());
//	}

	@Override
	public void load(BlockState state,CompoundTag tag) {
		super.load(state,tag);

		progress = tag.getInt("Progress");
		inventory.deserializeNBT(tag.getCompound("Inventory"));
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		super.save(tag);

		tag.putInt("Progress", progress);
		tag.put("Inventory", inventory.serializeNBT());
		return tag;
	}

	@Override
	public void tickServer() {
		super.tickServer();

		totalCost = 0L;
		craftingTime = 0;

		ItemStack output = inventory.getStackInSlot(1);
		boolean hasOutput = !output.isEmpty();

		if (inventory.getStackInSlot(0).isEmpty() || hasOutput && output.getCount() >= output.getMaxStackSize()) {
			return;
		}

		ItemStack input = inventory.getStackInSlot(0);
		findRecipeFor(input).ifPresent(recipe -> {
			ItemStack result = recipe.assemble(new SimpleContainer(input));
			if (!hasOutput || ItemHandlerHelper.canItemStacksStack(output, result)) {
				totalCost = recipe.getTotalCost(input);
				craftingTime = recipe.getCraftingTime();
				if (storedEMC >= totalCost) {
					if (++progress >= craftingTime) {
						storedEMC -= totalCost;
						progress = 0;
						inventory.extractItem(0, 1, false);
						ItemStack newOutput = hasOutput ? ItemHandlerHelper.copyStackWithSize(output, output.getCount() + 1) : result;
						inventory.setStackInSlot(1, newOutput);
					}
					setChanged();
				}
			}
		});
	}

	@NotNull
	@Override
	public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return itemCap.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public long insertEmc(long emc, EmcAction action) {
		if (!nonNullLevel().isClientSide()) {
			if (totalCost <= 0L) return 0L;
			long toInsert = Math.min(emc, getMaximumEmc() - storedEMC);
			storedEMC += toInsert;
			setChanged();
			return toInsert;
		}
		return 0L;
	}

	@Override
	public long getMaximumEmc() {
		return totalCost * 8L;
	}

	public ItemStackHandler getInventory() {
		return inventory;
	}

	private Optional<AlchemyTableRecipe> findRecipeFor(ItemStack input) {
		RecipeManager recipeManager = nonNullLevel().getRecipeManager();

		return RecipeCache.ALCHEMY.getCachedRecipe(nonNullLevel(), new SimpleContainer(input));
	}
//	recipeManager.getRecipeFor(ModRecipeTypes.ALCHEMY_TABLE, new SimpleContainer(input), nonNullLevel());

	@Override
	public NonNullList<ItemStack> getContentsToDrop() {
		NonNullList<ItemStack> res = NonNullList.create();
		for (int i = 0; i < inventory.getSlots(); i++) {
			if (!inventory.getStackInSlot(i).isEmpty()) res.add(inventory.getStackInSlot(i));
		}
		return res;
	}



	public class TrackedData implements ContainerData {
		@Override
		public int get(int index) {
            switch (index) {
                case 0:
                    return Math.min(255, craftingTime <= 0 ? 0 : (progress * 255 / craftingTime));
                case 1:
                    return Math.min(255, totalCost <= 0D ? 0 : (int) (storedEMC * 255L / totalCost));
                default:
                    return 0;
            }
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
				case 0:
					progressDisplay = value;
					break;
				case 1:
					costDisplay = value;
				default:
			}
		}

		@Override
		public int getCount() {
			return 2;
		}
	}

	private static class WrappedAlchemyHandler implements IItemHandler {
		private final ItemStackHandler wrapped;

		public WrappedAlchemyHandler(ItemStackHandler inventory) {
			this.wrapped = inventory;
		}

		@Override
		public int getSlots() {
			return wrapped.getSlots();
		}

		@NotNull
		@Override
		public ItemStack getStackInSlot(int slot) {
			return wrapped.getStackInSlot(slot);
		}

		@NotNull
		@Override
		public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
			return slot == 0 ? wrapped.insertItem(slot, stack, simulate) : stack;
		}

		@NotNull
		@Override
		public ItemStack extractItem(int slot, int count, boolean simulate) {
			return slot == 1 ? wrapped.extractItem(slot, count, simulate) : ItemStack.EMPTY;
		}

		@Override
		public int getSlotLimit(int slot) {
			return wrapped.getSlotLimit(slot);
		}

		@Override
		public boolean isItemValid(int slot, @NotNull ItemStack stack) {
			return wrapped.isItemValid(slot, stack);
		}
	}
}
