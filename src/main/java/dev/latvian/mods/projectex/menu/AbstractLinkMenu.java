package dev.latvian.mods.projectex.menu;


import dev.latvian.mods.projectex.block.entity.AbstractLinkInvBlockEntity;
import dev.latvian.mods.projectex.inventor.FilterSlot;
import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.api.ProjectEAPI;

import moze_intel.projecte.api.capabilities.tile.IEmcStorage;
import moze_intel.projecte.config.ProjectEConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public abstract class AbstractLinkMenu<T extends AbstractLinkInvBlockEntity> extends AbstractEXMenu<T> {
    public AbstractLinkMenu(MenuType<?> type, int windowId, Inventory invPlayer, BlockPos tilePos) {
        super(type, windowId, invPlayer, tilePos);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();

            long value = ProjectEAPI.getEMCProxy().getValue(stack);
            if (value == 0) return ItemStack.EMPTY;

            ItemStack oldStack = stack.copy();
            player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY).ifPresent(provider -> {
                ItemInfo fixed = ProjectEAPI.getEMCProxy().getPersistentInfo(ItemInfo.fromStack(stack));
                provider.addKnowledge(fixed);
                getBlockEntity().addToOutput(fixed.createStack());
                long actualEmc = (long) (stack.getCount() * value * ProjectEConfig.server.difficulty.covalenceLoss.get());
                getBlockEntity().insertEmc(actualEmc, IEmcStorage.EmcAction.EXECUTE);
            });

            slot.set(ItemStack.EMPTY);
            return oldStack;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack clicked(int slotId, int button, ClickType clickType, Player player) {
        if (player instanceof ServerPlayer && slotId >= 0 && slotId < slots.size()) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            Slot slot = slots.get(slotId);
            if (slot instanceof FilterSlot) {
                FilterSlot filterSlot = (FilterSlot) slot;
                switch (clickType) {
                    case QUICK_MOVE:
                        slot.set(ItemStack.EMPTY);
                        break;
                    case PICKUP:
                        if (!player.inventory.getCarried().isEmpty()) {
                            ItemStack fixed = ProjectEAPI.getEMCProxy().getPersistentInfo(ItemInfo.fromStack(player.inventory.getCarried())).createStack();

                            // prevent duplicate items in the filter slots
                            for (int i = 0; i < getBlockEntity().getOutputHandler().getSlots(); i++) {
                                if (getBlockEntity().getOutputHandler().getStackInSlot(i).getItem() == fixed.getItem()) {
                                    return fixed;
                                }
                            }
                            slot.set(fixed);
                            player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY).ifPresent(provider -> {
                                if (provider.addKnowledge(player.inventory.getCarried())) {
                                    provider.sync(serverPlayer);
                                }
                            });
                        } else if (slot.hasItem()) {
                            int amount = button == 0 ? slot.getItem().getMaxStackSize() : 1;
                            ItemStack extracted = filterSlot.remove(amount);
                            ItemHandlerHelper.giveItemToPlayer(player, extracted);
                        }
                        break;
                }

                return null;
            }
        }
        return super.clicked(slotId, button, clickType, player);
    }
}
