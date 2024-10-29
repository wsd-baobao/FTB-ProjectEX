package dev.latvian.mods.projectex.integration.jei;

import net.minecraftforge.fml.ModList;

public class JEIHooks {

    public static void handleJEISync(String text) {
        //text为搜索框中的文本
        //检查JEI模组是否加载，如果加载则调用JEI的API
        if (ModList.get().isLoaded("jei")) {
            if (text != null) {
                JEIIntegration.setFilterText(text);
            }

        }
    }
}
