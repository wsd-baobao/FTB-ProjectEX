package dev.latvian.mods.projectex.menu;

import dev.latvian.mods.projectex.block.entity.AlchemyTableEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class AlchemyTableMenu extends AbstractEXMenu<AlchemyTableEntity>{
    public AlchemyTableMenu(MenuType<?> type, int windowId, Inventory invPlayer, BlockPos blockPos) {
        super(type, windowId, invPlayer, blockPos);
        addSlot(new SlotItemHandler(getBlockEntity().getInventory(), 0, 44, 35));
        addSlot(new OutputSlot(getBlockEntity().getInventory(), 1, 116, 35));

        addPlayerSlots(invPlayer, 8, 84);

        addDataSlots(getBlockEntity().trackedData);
    }

    public AlchemyTableMenu(int i, Inventory inventory, BlockPos pos) {
        this(ModMenuTypes.ALCHEMY_TABLE.get(), i, inventory, pos);

    }

    public AlchemyTableMenu(int i, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(ModMenuTypes.ALCHEMY_TABLE.get(), i, inventory, friendlyByteBuf.readBlockPos());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot srcSlot = slots.get(index);
        if (!srcSlot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack srcStack = srcSlot.getItem().copy();
        ItemStack srcStackCopy = srcStack.copy();

        if (index < playerSlotsStart) {
            // shift-clicking out of machine
            if (!moveItemStackToHotbarOrInventory(srcStack, playerSlotsStart))
                return ItemStack.EMPTY;
        } else {
            // shift-clicking out of player inventory
            if (!moveItemStackTo(srcStack, 0, playerSlotsStart, false))
                return ItemStack.EMPTY;
        }

        srcSlot.set(srcStack);
        srcSlot.onQuickCraft(srcStack, srcStackCopy);
        srcSlot.onTake(player, srcStack);

        return srcStackCopy;
    }

    boolean moveItemStackToHotbarOrInventory(ItemStack stack, int startIndex) {
        // when shift-clicking out of the machine, try to move to hotbar first, then to rest of inventory
        return moveItemStackTo(stack, startIndex + 27, startIndex + 36, false)
                || moveItemStackTo(stack, startIndex, startIndex + 27, false);
    }

    @NotNull
    @Override
    protected Class<AlchemyTableEntity> blockEntityClass() {
        return AlchemyTableEntity.class;
    }
}
