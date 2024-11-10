package dev.latvian.mods.projectex.menu;

import dev.latvian.mods.projectex.block.entity.RefinedLinkBlockEntity;
import dev.latvian.mods.projectex.inventor.FilterSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class RefinedLinkMenu extends AbstractLinkMenu<RefinedLinkBlockEntity>{
    public RefinedLinkMenu(MenuType<?> type, int windowId, Inventory invPlayer, BlockPos tilePos) {
        super(type, windowId, invPlayer, tilePos);
        addSlot(new SlotItemHandler(getBlockEntity().getInputHandler(), 0, 35, 35));

        for (int i = 0; i < 9; i++) {
            addSlot(new FilterSlot(getBlockEntity().getOutputHandler(), i, 89 + (i % 3) * 18, 17 + (i / 3) * 18));
        }

        addPlayerSlots(invPlayer, 8, 84);
    }
    public RefinedLinkMenu(int windowId, Inventory playerInv, FriendlyByteBuf buf) {
        this(ModMenuTypes.REFINED_LINK.get(),windowId, playerInv, buf.readBlockPos());
    }



    @Override
    protected @NotNull Class<RefinedLinkBlockEntity> blockEntityClass() {
        return RefinedLinkBlockEntity.class;
    }
}
