package dev.latvian.mods.projectex.block.entity;

import dev.latvian.mods.projectex.menu.ModMenuTypes;
import dev.latvian.mods.projectex.menu.PersonalLinkMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

public class PersonalLinkBlockEntity extends AbstractLinkInvBlockEntity implements MenuProvider {
    public PersonalLinkBlockEntity() {
        super(ProjectEXBlockEntities.PERSONAL_LINK.get(),18, 1);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("block.projectex.personal_link");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player arg2) {
        return new PersonalLinkMenu(windowId, playerInv, getBlockPos());
    }
}
