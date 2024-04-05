package dev.latvian.mods.projectex;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class EXTags {
    public static class Items {
        public static final Tag<Item> STONE_TABLE_WHITELIST = ItemTags.createOptional(new ResourceLocation(ProjectEX.MOD_ID, "stone_table_whitelist"));

//        static Tag<Item> tag(String modid, String name) {
//            return Objects.requireNonNull(ItemTags.createOptional(new ResourceLocation(modid, name)));
//        }
//
//        static Tag<Item> modTag(String name) {
//            return tag(ProjectEX.MOD_ID, name);
//        }
//
//        static Tag<Item> forgeTag(String name) {
//            return tag("forge", name);
//        }
    }
}
