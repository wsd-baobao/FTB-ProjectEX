package dev.latvian.mods.projectex.config;


import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigInteger;

public class ConfigHolder {
    static ClientConfig client;
    static ServerConfig server;
    private static ForgeConfigSpec configClientSpec;
    private static ForgeConfigSpec configServerSpec;

    public static void init() {
        final Pair<ClientConfig, ForgeConfigSpec> clientSpec = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        client = clientSpec.getLeft();
        configClientSpec = clientSpec.getRight();

        final Pair<ServerConfig, ForgeConfigSpec> serverSpec = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        server = serverSpec.getLeft();
        configServerSpec = serverSpec.getRight();

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHolder.configClientSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigHolder.configServerSpec);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ConfigHolder::onConfigChanged);
    }

    private static void onConfigChanged(final ModConfig.ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if (config.getSpec() == ConfigHolder.configClientSpec) {
            refreshClient();
        } else if (config.getSpec() == ConfigHolder.configServerSpec) {
            refreshServer();
        }
    }

    private static void refreshServer() {
        ConfigHelper.emcLinkMaxOutput = BigInteger.valueOf(ConfigHelper.server().general.emcLinkMaxOutput.get());
    }

    private static void refreshClient() {
    }
}
