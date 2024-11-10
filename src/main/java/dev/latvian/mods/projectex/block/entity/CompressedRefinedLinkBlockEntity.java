package dev.latvian.mods.projectex.block.entity;

import dev.latvian.mods.projectex.menu.CompressedRefinedLinkMenu;
import dev.latvian.mods.projectex.menu.ModMenuTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

public class CompressedRefinedLinkBlockEntity extends AbstractLinkInvBlockEntity implements MenuProvider {
	public CompressedRefinedLinkBlockEntity() {
		super(ProjectEXBlockEntities.COMPRESSED_REFINED_LINK.get(), 1, 54);
	}

	@Override
	public Component getDisplayName() {
		return new TranslatableComponent("block.projectex.compressed_refined_link");
	}

	@Override
	public @Nullable AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player arg2) {
		return new CompressedRefinedLinkMenu(ModMenuTypes.COMPRESSED_REFINED_LINK.get(),windowId, playerInv, getBlockPos());
	}
}
