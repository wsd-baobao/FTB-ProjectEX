package dev.latvian.mods.projectex.item;

import dev.latvian.mods.projectex.Matter;
import dev.latvian.mods.projectex.ProjectEX;
import dev.latvian.mods.projectex.Star;
import dev.latvian.mods.projectex.block.ModBlocks;
import net.minecraft.Util;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public interface ModItems {
	DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ProjectEX.MOD_ID);

	static Supplier<BlockItem> blockItem(String id, Supplier<Block> sup) {
		return REGISTRY.register(id, () -> new BlockItem(sup.get(), new Item.Properties().tab(ProjectEX.tab)));
	}

	Supplier<BlockItem> ENERGY_LINK = blockItem("energy_link", ModBlocks.ENERGY_LINK);
	Supplier<BlockItem> PERSONAL_LINK = blockItem("personal_link", ModBlocks.PERSONAL_LINK);
	Supplier<BlockItem> REFINED_LINK = blockItem("refined_link", ModBlocks.REFINED_LINK);
	Supplier<BlockItem> COMPRESSED_REFINED_LINK = blockItem("compressed_refined_link", ModBlocks.COMPRESSED_REFINED_LINK);

	Map<Matter, Supplier<BlockItem>> COLLECTOR = Util.make(new LinkedHashMap<>(), map -> {
		for (Matter matter : Matter.VALUES) {
			map.put(matter, blockItem(matter.name + "_collector", ModBlocks.COLLECTOR.get(matter)));
		}
	});

	Map<Matter, Supplier<BlockItem>> RELAY = Util.make(new LinkedHashMap<>(), map -> {
		for (Matter matter : Matter.VALUES) {
			map.put(matter, blockItem(matter.name + "_relay", ModBlocks.RELAY.get(matter)));
		}
	});

	Map<Matter, Supplier<BlockItem>> POWER_FLOWER = Util.make(new LinkedHashMap<>(), map -> {
		for (Matter matter : Matter.VALUES) {
			map.put(matter, blockItem(matter.name + "_power_flower", ModBlocks.POWER_FLOWER.get(matter)));
		}
	});

	Supplier<BlockItem> STONE_TABLE = blockItem("stone_table", ModBlocks.STONE_TABLE);
	Supplier<BlockItem> ALCHEMY_TABLE = blockItem("alchemy_table", ModBlocks.ALCHEMY_TABLE);

	Map<Star, Supplier<Item>> MAGNUM_STAR = Util.make(new LinkedHashMap<>(), map -> {
		for (Star star : Star.VALUES) {
			map.put(star, REGISTRY.register("magnum_star_" + star.name, () -> new MagnumStarItem(star)));
		}
	});

	Map<Star, Supplier<Item>> COLOSSAL_STAR = Util.make(new LinkedHashMap<>(), map -> {
		for (Star star : Star.VALUES) {
			map.put(star, REGISTRY.register("colossal_star_" + star.name, () -> new ColossalStarItem(star)));
		}
	});

	Map<Matter, Supplier<Item>> MATTER = Util.make(new LinkedHashMap<>(), map -> {
		for (Matter matter : Matter.VALUES) {
			if (matter.hasMatterItem) {
				map.put(matter, REGISTRY.register(matter.name + "_matter", BasicItem::new));
			}
		}
	});

	Supplier<Item> FINAL_STAR_SHARD = REGISTRY.register("final_star_shard", BasicItem::new);
	Supplier<Item> FINAL_STAR = REGISTRY.register("final_star", FinalStarItem::new);
	Supplier<Item> KNOWLEDGE_SHARING_BOOK = REGISTRY.register("knowledge_sharing_book", KnowledgeSharingBookItem::new);
	Supplier<Item> ARCANE_TABLET = REGISTRY.register("arcane_tablet", ArcaneTabletItem::new);

	Map<Matter, Supplier<Item>> COMPRESSED_COLLECTOR = Util.make(new LinkedHashMap<>(), map -> {
		for (Matter matter : Matter.VALUES) {
			map.put(matter, REGISTRY.register(matter.name + "_compressed_collector", FoilItem::new));
		}
	});
}
