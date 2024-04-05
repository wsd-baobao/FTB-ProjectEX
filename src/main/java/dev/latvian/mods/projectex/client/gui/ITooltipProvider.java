package dev.latvian.mods.projectex.client.gui;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

import java.util.List;

public interface ITooltipProvider {
    void addTooltip(double mouseX, double mouseY, List<Component> curTip, boolean shift);

    default boolean shouldProvide() {
        return this instanceof AbstractWidget && (((AbstractWidget) this).isFocused() || ((AbstractWidget) this).isHovered()) && ((AbstractWidget) this).visible;
    }
}
