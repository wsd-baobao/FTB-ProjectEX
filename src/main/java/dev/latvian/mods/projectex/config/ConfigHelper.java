package dev.latvian.mods.projectex.config;

import dev.latvian.mods.projectex.EXTags;
import dev.latvian.mods.projectex.ProjectEX;
import net.minecraft.world.item.ItemStack;

import java.math.BigInteger;

public class ConfigHelper {
    static BigInteger emcLinkMaxOutput = BigInteger.ZERO;

    public static ClientConfig client() {
        return ConfigHolder.client;
    }

    public static ServerConfig server() {
        return ConfigHolder.server;
    }

    public static void setSearchType(SearchType searchType) {
        client().general.searchType.set(searchType);
    }

    public static boolean isStoneTableWhitelisted(ItemStack stack) {
        ProjectEX.LOGGER.info("---------------------------"+stack);
//        ProjectEX.LOGGER.info("isStoneTableWhitelisted: " + EXTags.Items.STONE_TABLE_WHITELIST.contains(stack.getItem()));
//        ProjectEX.LOGGER.info("isStoneTableWhitelisted: " + server().general.enableStoneTableWhitelist.get());
//
//
        return  !server().general.enableStoneTableWhitelist.get() ||(EXTags.Items.STONE_TABLE_WHITELIST.contains(stack.getItem()));
    }

    public static BigInteger getEMCLinkMaxOutput() {
        return emcLinkMaxOutput;
    }

    public static int getEMCLinkMaxStackSize() {
        return server().general.emcLinkMaxStackSize.get();
    }
}
