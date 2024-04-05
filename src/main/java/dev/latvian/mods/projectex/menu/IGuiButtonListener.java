package dev.latvian.mods.projectex.menu;

import net.minecraft.server.level.ServerPlayer;

public interface IGuiButtonListener {
    void handleGUIButtonPress(String tag, boolean shiftHeld, ServerPlayer player);
}
