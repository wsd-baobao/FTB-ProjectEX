package dev.latvian.mods.projectex.client;

import dev.latvian.mods.projectex.client.gui.KnowledgeUpdateListener;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ClientUtils {
    public static Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    public static void onknowledgeUpdate() {
        if (Minecraft.getInstance().screen instanceof KnowledgeUpdateListener) {
            KnowledgeUpdateListener k = (KnowledgeUpdateListener) Minecraft.getInstance().screen;
            k.onKnowledgeUpdate();
        }
    }
}
