package dev.latvian.mods.projectex.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.projectex.ProjectEX;
import dev.latvian.mods.projectex.client.gui.buttons.ArrowButton;
import dev.latvian.mods.projectex.client.gui.buttons.ExtractItemButton;
import dev.latvian.mods.projectex.client.gui.buttons.HighlightButton;
import dev.latvian.mods.projectex.client.gui.buttons.SearchTypeButton;
import dev.latvian.mods.projectex.menu.ArcaneTabletMenu;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ArcaneTabletScreen extends AbstractTableScreen<ArcaneTabletMenu>{
    private static final ResourceLocation TEXTURE = new ResourceLocation(ProjectEX.MOD_ID, "textures/gui/arcane_tablet.png");

    public ArcaneTabletScreen(ArcaneTabletMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

    }

    @Override
    protected void init() {
        super.init();

        addButton(new ArrowButton(leftPos + 7, topPos + 20, b -> changePage(false))
                .withTexture(TEXTURE, 196, 0));
        addButton(new ArrowButton(leftPos + 151, topPos + 20, b -> changePage(true))
                .withTexture(TEXTURE, 215, 0));

        addButton(new HighlightButton(leftPos + 80, topPos + 68).withTag("burn"));

        extractButtons.clear();
        addExtractButton(new ExtractItemButton(leftPos + 80, topPos + 20, menu.getProvider()));
        addExtractButton(new ExtractItemButton(leftPos + 105, topPos + 26, menu.getProvider()));
        addExtractButton(new ExtractItemButton(leftPos + 55, topPos + 26, menu.getProvider()));
        addExtractButton(new ExtractItemButton(leftPos + 123, topPos + 44, menu.getProvider()));
        addExtractButton(new ExtractItemButton(leftPos + 37, topPos + 44, menu.getProvider()));
        addExtractButton(new ExtractItemButton(leftPos + 128, topPos + 68, menu.getProvider()));
        addExtractButton(new ExtractItemButton(leftPos + 32, topPos + 68, menu.getProvider()));
        addExtractButton(new ExtractItemButton(leftPos + 123, topPos + 92, menu.getProvider()));
        addExtractButton(new ExtractItemButton(leftPos + 37, topPos + 92, menu.getProvider()));
        addExtractButton(new ExtractItemButton(leftPos + 105, topPos + 110, menu.getProvider()));
        addExtractButton(new ExtractItemButton(leftPos + 55, topPos + 110, menu.getProvider()));
        addExtractButton(new ExtractItemButton(leftPos + 80, topPos + 116, menu.getProvider()));

        addButton(new HighlightButton(leftPos + 9, topPos + 116)
                .withTag("learn").withTooltip(new TranslatableComponent("block.projectex.stone_table.learn")));
        addButton(new HighlightButton(leftPos + 153, topPos + 116)
                .withTag("unlearn").withTooltip(new TranslatableComponent("block.projectex.stone_table.unlearn")));

        addButton(new HighlightButton(leftPos - 71, topPos + 16, 9, 9)
                .withTag("rotate").withTooltip(new TranslatableComponent("gui.arcane_tablet.rotate")));
        addButton(new HighlightButton(leftPos - 71, topPos + 26, 9, 9)
                .withTag("balance").withTooltip(new TranslatableComponent("gui.arcane_tablet.balance")));
        addButton(new HighlightButton(leftPos - 71, topPos + 61, 9, 9)
                .withTag("clear").withTooltip(new TranslatableComponent("gui.arcane_tablet.clear")));

        addButton(new SearchTypeButton(leftPos - 71, topPos + 36));

        updateValidItemList();
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
        super.renderBg(poseStack, partialTick, mouseX, mouseY);
        blit(poseStack, leftPos - 75, topPos + 10, 180, 19, 76, 89);
    }



    @Override
    protected ResourceLocation getGuiTexture() {
        return TEXTURE;
    }


    @Override
    protected Rect2i searchFieldPos() {
        return new Rect2i(leftPos + 8, topPos + 7, 160, 11);
    }
}
