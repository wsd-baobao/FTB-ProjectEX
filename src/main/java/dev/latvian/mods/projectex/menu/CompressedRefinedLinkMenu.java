package dev.latvian.mods.projectex.menu;

import dev.latvian.mods.projectex.block.entity.CompressedRefinedLinkBlockEntity;
import dev.latvian.mods.projectex.inventor.FilterSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class CompressedRefinedLinkMenu extends AbstractLinkMenu<CompressedRefinedLinkBlockEntity> {
    public CompressedRefinedLinkMenu(MenuType<?> type, int windowId, Inventory playerInv, BlockPos tilePos) {
        super(type, windowId, playerInv, tilePos);
        addSlot(new SlotItemHandler(getBlockEntity().getInputHandler(), 0, 8, 17));

        for (int i = 0; i < 54; i++) {
            addSlot(new FilterSlot(getBlockEntity().getOutputHandler(), i, 8 + (i % 9) * 18, 41 + (i / 9) * 18));
        }

        addPlayerSlots(playerInv, 8, 162);
    }
    public CompressedRefinedLinkMenu(int windowId, Inventory playerInv, FriendlyByteBuf buf) {
        this(ModMenuTypes.COMPRESSED_REFINED_LINK.get(),windowId, playerInv, buf.readBlockPos());
    }
    @Override
    protected @NotNull Class<CompressedRefinedLinkBlockEntity> blockEntityClass() {
        return CompressedRefinedLinkBlockEntity.class;
    }
}
