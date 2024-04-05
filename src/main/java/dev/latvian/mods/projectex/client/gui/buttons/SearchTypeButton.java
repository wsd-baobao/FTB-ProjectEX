package dev.latvian.mods.projectex.client.gui.buttons;


import dev.latvian.mods.projectex.config.ConfigHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.List;

public class SearchTypeButton extends HighlightButton {
    public SearchTypeButton(int x, int y) {
        super(x, y, 9, 9, b -> updateSearchType());
    }

    private static void updateSearchType() {
        ConfigHelper.setSearchType(ConfigHelper.client().general.searchType.get().cycle());
    }

    @Override
    public void addTooltip(double mouseX, double mouseY, List<Component> curTip, boolean shift) {
        curTip.add(new TranslatableComponent("projectex.general.search_type"));
        curTip.add(new TranslatableComponent(ConfigHelper.client().general.searchType.get().translationKey).withStyle(ChatFormatting.GRAY));
    }
}
