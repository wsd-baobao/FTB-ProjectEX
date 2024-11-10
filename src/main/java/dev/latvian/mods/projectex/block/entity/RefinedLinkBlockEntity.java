package dev.latvian.mods.projectex.block.entity;

import dev.latvian.mods.projectex.menu.ModMenuTypes;
import dev.latvian.mods.projectex.menu.RefinedLinkMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

public class RefinedLinkBlockEntity extends AbstractLinkInvBlockEntity implements MenuProvider {
	public RefinedLinkBlockEntity() {
		super(ProjectEXBlockEntities.REFINED_LINK.get(), 1, 9);
	}

	@Override
	public Component getDisplayName() {
		return new TranslatableComponent("block.projectex.refined_link");
	}

	@Override
	public @Nullable AbstractContainerMenu createMenu(int windowId, Inventory playerInv, Player arg2) {
		//第一个参数删除和添加好像没有什么区别，三级接口删除没什么异常。
		return new RefinedLinkMenu(ModMenuTypes.REFINED_LINK.get(), windowId, playerInv, getBlockPos());
	}

	@Override
	protected boolean learnItems() {
		return true;
	}
}
