package dev.latvian.mods.projectex.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector4f;
import dev.latvian.mods.projectex.ProjectEX;
import dev.latvian.mods.projectex.block.entity.AlchemyTableEntity;
import dev.latvian.mods.projectex.menu.AlchemyTableMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;



public class AlchemyTableScreen extends AbstractEXScreen<AlchemyTableMenu, AlchemyTableEntity> {
private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ProjectEX.MOD_ID, "textures/gui/alchemy_table.png");

public AlchemyTableScreen(AlchemyTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        }

        @Override
        protected ResourceLocation getGuiTexture() {
                return BACKGROUND_TEXTURE;
        }
@Override
protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(poseStack, partialTicks, mouseX, mouseY);

        if (menu.getBlockEntity().costDisplay > 0) {
                blit(poseStack,leftPos + 77, topPos + 34, 177, 17, Math.max(1, (int) (menu.getBlockEntity().costDisplay / 255F * 24F)), 18);
        }
        if (menu.getBlockEntity().progressDisplay > 0) {
                blit(poseStack,leftPos + 78, topPos + 35, 177, 0, Math.max(1, (int) (menu.getBlockEntity().progressDisplay / 255F * 22F)), 16);
        }
        }


}
