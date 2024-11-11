package dev.latvian.mods.projectex.client;

import dev.latvian.mods.projectex.client.gui.*;
import dev.latvian.mods.projectex.menu.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientSetup {
    public static void initEarly() {
        // run on mod construction
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::init);
    }

    static void init(FMLClientSetupEvent event) {
        event.enqueueWork(ClientSetup::initLate);
    }
    private static void initLate() {
        // stuff that needs doing on the main thread
        registerScreenFactories();
    }

    private static void registerScreenFactories() {

        MenuScreens.register(ModMenuTypes.ALCHEMY_TABLE.get(), AlchemyTableScreen::new);
        MenuScreens.register(ModMenuTypes.ARCANE_TABLET.get(), ArcaneTabletScreen::new);
        MenuScreens.register(ModMenuTypes.STONE_TABLE.get(), StoneTableScreen::new);
        MenuScreens.register(ModMenuTypes.PERSONAL_LINK.get(), PersonalLinkScreen::new);
        MenuScreens.register(ModMenuTypes.REFINED_LINK.get(), RefinedLinkScreen::new);
        MenuScreens.register(ModMenuTypes.COMPRESSED_REFINED_LINK.get(), CompressedRefinedLinkScreen::new);


    }
}
