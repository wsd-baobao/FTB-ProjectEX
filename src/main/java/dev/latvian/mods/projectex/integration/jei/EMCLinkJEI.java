package dev.latvian.mods.projectex.integration.jei;

import com.google.common.collect.ImmutableList;
import dev.latvian.mods.projectex.block.entity.AbstractEMCBlockEntity;
import dev.latvian.mods.projectex.client.ClientUtils;
import dev.latvian.mods.projectex.client.gui.AbstractEXScreen;
import dev.latvian.mods.projectex.inventor.FilterSlot;
import dev.latvian.mods.projectex.menu.AbstractEXMenu;
import dev.latvian.mods.projectex.network.NetworkHandler;
import dev.latvian.mods.projectex.network.PacketJEIGhost;
import dev.latvian.mods.projectex.util.EXUtils;
import mezz.jei.api.gui.handlers.IGhostIngredientHandler;
import moze_intel.projecte.api.ProjectEAPI;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class EMCLinkJEI <S extends AbstractEXScreen<M,T>, M extends AbstractEXMenu<T>, T extends AbstractEMCBlockEntity> implements IGhostIngredientHandler<S> {


    @Override
    public <I> List<Target<I>> getTargets(S gui, I ingredient, boolean doStart) {
        ImmutableList.Builder<Target<I>> builder = ImmutableList.builder();
        if (ingredient instanceof ItemStack
                && ProjectEAPI.getEMCProxy().hasValue((ItemStack) ingredient)
                && EXUtils.playerHasKnowledge(ClientUtils.getClientPlayer(), (ItemStack) ingredient))
        {
            ItemStack stack = (ItemStack) ingredient;
            NonNullList<Slot> slots = (NonNullList<Slot>) gui.getMenu().slots;
            // indexed for loop needed to get raw container slot index
            for (int i = 0; i < slots.size(); i++) {
                Slot slot = slots.get(i);
                if (slot instanceof FilterSlot) {
                    FilterSlot filterSlot = (FilterSlot) slot;
                    builder.add(new ItemStackTarget<>(filterSlot, i, gui));
                }
            }
        }
        return builder.build();
    }

    @Override
    public void onComplete() {
    }

    public static class ItemStackTarget<I> implements Target<I> {
        private final FilterSlot filterSlot;
        private final int rawSlot;
        private final AbstractEXScreen<?, ?> gui;

        public ItemStackTarget(FilterSlot filterSlot, int rawSlot, AbstractEXScreen<?, ?> gui) {
            this.filterSlot = filterSlot;
            this.rawSlot = rawSlot;
            this.gui = gui;
        }

        @Override
        public Rect2i getArea() {
            return new Rect2i(gui.getGuiLeft() + filterSlot.x, gui.getGuiTop() + filterSlot.y, 16, 16);
        }

        @Override
        public void accept(I ingredient) {
            if (ingredient instanceof ItemStack) {
                ItemStack stack = (ItemStack) ingredient;
                filterSlot.set(stack);
                NetworkHandler.sendToServer(new PacketJEIGhost(rawSlot, stack));
            }
        }
    }





}
