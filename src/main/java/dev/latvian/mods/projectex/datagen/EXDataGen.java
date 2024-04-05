package dev.latvian.mods.projectex.datagen;


import dev.latvian.mods.projectex.ProjectEX;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = ProjectEX.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EXDataGen {
    public static final String MODID = ProjectEX.MOD_ID;

    @SubscribeEvent
    public static void dataGenEvent(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        ExistingFileHelper efh = event.getExistingFileHelper();

        if (event.includeClient()) {
            gen.addProvider(new ModLangProvider(gen, MODID, "en_us"));
            gen.addProvider(new ModBlockStateProvider(gen, MODID, efh));
            gen.addProvider(new ModBlockModelProvider(gen, MODID, efh));
            gen.addProvider(new ModItemModelProvider(gen, MODID, efh));
            gen.addProvider(new ModRecipeProvider(gen));
        }

        if (event.includeServer()) {
            ModBlockTagsProvider blockTags = new ModBlockTagsProvider(gen, MODID, efh);
            gen.addProvider(blockTags);
            gen.addProvider(new ModItemTagsProvider(gen, blockTags, MODID, efh));
            gen.addProvider(new ModRecipeProvider(gen));
            gen.addProvider(new ModLootTableProvider(gen));
        }
    }
}
