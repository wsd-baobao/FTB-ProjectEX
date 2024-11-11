package dev.latvian.mods.projectex.datagen;


import dev.latvian.mods.projectex.Matter;
import dev.latvian.mods.projectex.Star;
import dev.latvian.mods.projectex.block.ModBlocks;
import dev.latvian.mods.projectex.item.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

class ModLangProvider extends LanguageProvider {
    public ModLangProvider(DataGenerator gen, String modid, String locale) {
        super(gen, modid, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.projectex", "ProjectEX");
        addBlock(ModBlocks.ENERGY_LINK, "Energy EMC Link");
        addBlock(ModBlocks.PERSONAL_LINK, "Personal EMC Link");
        addBlock(ModBlocks.REFINED_LINK, "Refined EMC Link");
        addBlock(ModBlocks.COMPRESSED_REFINED_LINK, "Compressed Refined EMC Link");

        for (Matter matter : Matter.VALUES) {
            if (matter.hasMatterItem) {
                addItem(matter.getItem(), matter.displayName + " Matter");
            }

            addBlock(ModBlocks.COLLECTOR.get(matter), matter.displayName + " Energy Collector " + matter.getMK());
            addBlock(ModBlocks.RELAY.get(matter), matter.displayName + " Anti-Matter Relay " + matter.getMK());
            addItem(ModItems.COMPRESSED_COLLECTOR.get(matter), matter.displayName + " Compressed Energy Collector " + matter.getMK());
            addBlock(ModBlocks.POWER_FLOWER.get(matter), matter.displayName + " Power Flower " + matter.getMK());
        }

        addBlock(ModBlocks.STONE_TABLE, "Stone Transmutation Table");
        addBlock(ModBlocks.ALCHEMY_TABLE, "Alchemy Table");

        for (Star star : Star.VALUES) {
            addItem(ModItems.MAGNUM_STAR.get(star), "Magnum Star " + star.displayName);
            addItem(ModItems.COLOSSAL_STAR.get(star), "Colossal Star " + star.displayName);
        }

        addItem(ModItems.FINAL_STAR_SHARD, "Final Star Shard");
        addItem(ModItems.FINAL_STAR, "The Final Star");
        addItem(ModItems.KNOWLEDGE_SHARING_BOOK, "Knowledge Sharing Book");
        addItem(ModItems.ARCANE_TABLET, "Arcane Transmutation Tablet");

        add("block.projectex.energy_link.tooltip", "You can use this block to add EMC to your Transmutation Table using Collectors.");
        add("block.projectex.personal_link.tooltip", "Like the Basic Energy EMC Link, but also allows to import and export items.");
        add("block.projectex.refined_link.tooltip", "Like the Personal EMC Link, but has 1 input slot and 9 output slots. Intended as an automated item storage system for mods like Refined Storage or AE2. Learns items from input slots.");
        add("block.projectex.compressed_refined_link.tooltip", "Like the Refined EMC Link, but has 54 output slots.");

        add("block.projectex.collector.tooltip", "Server TPS friendly. Generates EMC only once a second.");
        add("block.projectex.collector.emc_produced", "Produced EMC: %s/s");

        add("block.projectex.relay.tooltip", "Server TPS friendly. Transfers EMC only once a second.");
        add("block.projectex.relay.max_transfer", "Max EMC Transfer: %s/s");
        add("block.projectex.relay.relay_bonus", "Relay Bonus: %s/s");

        add("block.projectex.stone_table.tooltip", "An alternative to ProjectE's Transmutation Table");
        add("block.projectex.stone_table.learn", "Learn");
        add("block.projectex.stone_table.unlearn", "Unlearn");
        add("gui.projectex.stone_table.cant_use", "Can't use this item in Stone Table!");

        add("item.projectex.final_star.tooltip", "Infinite EMC source");
        add("item.projectex.final_star.pedestal", "Place a chest next to Pedestal and drop an item on top of it to clone it infinitely.");

        add("item.projectex.knowledge_sharing_book.tooltip.1", "Sneak + Right-Click to set yourself as owner");
        add("item.projectex.knowledge_sharing_book.tooltip.2", "Right-Click to copy knowledge from the book's owner to yourself");
        add("item.projectex.knowledge_sharing_book.learned", "Knowledge copied! %d item(s) learned");

        add("item.projectex.arcane_tablet.tooltip", "Portable Transmutation Tablet with built-in crafting functionality");
        add("gui.projectex.arcane_tablet.rotate", "Rotate");
        add("gui.projectex.arcane_tablet.balance", "Balance / Spread");
        add("gui.projectex.arcane_tablet.clear", "Clear");

        add("gui.projectex.link.tooltip.1", "Left-click: Extract stack");
        add("gui.projectex.link.tooltip.2", "Right-click: Extract item");
        add("gui.projectex.link.tooltip.3", "Shift + Right-click: Clear slot");

        add("projectex.general.search_type", "Search Type");
        add("projectex.general.search_type.normal", "Normal");
        add("projectex.general.search_type.autoselected", "Auto-selected");
        add("projectex.general.search_type.normal_jei_sync", "Normal (JEI Sync)");
        add("projectex.general.search_type.autoselected_jei_sync", "Auto-selected (JEI Sync)");

        /*
        extendedexchange.general.enable_stone_table_whitelist=Enable Stone Table Whitelist
        extendedexchange.general.override_emc_formatter=Override EMC Formatter
        extendedexchange.general.blacklist_power_flower_from_watch=Blacklist Power Flower from Watch
        extendedexchange.general.final_star_copy_any_item=Final Star Can Copy Any Item
        extendedexchange.general.final_star_copy_nbt=Final Star Can Copy NBT
        extendedexchange.general.emc_link_max_out=EMC Link Max Item Output
         */
    }
}
