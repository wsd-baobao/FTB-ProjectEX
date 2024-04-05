package dev.latvian.mods.projectex.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.projectex.block.entity.AbstractEMCBlockEntity;
import dev.latvian.mods.projectex.block.entity.CollectorBlockEntity;
import dev.latvian.mods.projectex.menu.AbstractEXMenu;
import mezz.jei.render.IngredientRenderHelper;
import moze_intel.projecte.gameObjs.gui.AbstractCollectorScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class AbstractEXScreen<C extends AbstractEXMenu<T> , T extends AbstractEMCBlockEntity> extends AbstractContainerScreen<C> {

    public AbstractEXScreen(C menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    public static void bindTexture(ResourceLocation guiTexture) {
        Minecraft.getInstance().getTextureManager().bind(guiTexture);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.blendColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableTexture();

    }
    final void bindGuiTexture() {
        ResourceLocation guiTexture = getGuiTexture();
        if (guiTexture != null) {
            bindTexture(guiTexture);
        }
    }

    protected ResourceLocation getGuiTexture() {
        return null;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {

        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTick);
        this.renderTooltip(poseStack, mouseX, mouseY);


        List<Component> tooltip = new ArrayList<>();
        children.stream()
                .filter(w -> w instanceof ITooltipProvider && ((ITooltipProvider) w).shouldProvide())
                .forEach(w -> ((ITooltipProvider) w).addTooltip(mouseX, mouseY, tooltip, Screen.hasShiftDown()));
        if (!tooltip.isEmpty()) {
            renderComponentTooltip(poseStack, tooltip, mouseX, mouseY);
        }
      //  renderHeldItem(poseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {

        bindGuiTexture();
        int xStart = (width - imageWidth) / 2;
        int yStart = (height - imageHeight) / 2;
        blit(poseStack, xStart, yStart, 0, 0, imageWidth, imageHeight);
    }

    private void renderHeldItem(PoseStack matrix, int mouseX, int mouseY) {
        ItemStack heldItem = Minecraft.getInstance().player.inventory.getCarried(); // 获取鼠标上的物品
        if (!heldItem.isEmpty()) {
            // 设置渲染矩阵
            matrix.pushPose();
            matrix.translate(mouseX - 8, mouseY - 8, 0);

            // 渲染物品
            itemRenderer.renderGuiItem(heldItem, mouseX, mouseY);

            // 恢复渲染矩阵
            matrix.popPose();
        }
    }

}


