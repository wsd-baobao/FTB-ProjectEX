package dev.latvian.mods.projectex;

import dev.latvian.mods.projectex.block.ModBlocks;
import dev.latvian.mods.projectex.block.entity.ProjectEXBlockEntities;
import dev.latvian.mods.projectex.client.ClientSetup;
import dev.latvian.mods.projectex.config.ConfigHolder;
import dev.latvian.mods.projectex.item.ModItems;
import dev.latvian.mods.projectex.menu.ModMenuTypes;
import dev.latvian.mods.projectex.network.NetworkHandler;
import dev.latvian.mods.projectex.recipes.ModRecipeSerializers;
import dev.latvian.mods.projectex.recipes.ModRecipeTypes;
import dev.latvian.mods.projectex.recipes.RecipeCache;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

@Mod(ProjectEX.MOD_ID)
public class ProjectEX {
	public static final String MOD_ID = "projectex";

	//public static ProjectEXCommon PROXY;
	public static final Logger LOGGER = (Logger) LogManager.getLogger();
	public static final Direction[] DIRECTIONS = Direction.values();

	public static CreativeModeTab tab;

	public ProjectEX() {
		//PROXY = DistExecutor.safeRunForDist(() -> FTBJarModClient::new, () -> FTBJarModCommon::new);

		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSetup::initEarly);
		tab = new CreativeModeTab(MOD_ID) {
			@Override
			@OnlyIn(Dist.CLIENT)
			public ItemStack makeIcon() {
				return new ItemStack(ModItems.ARCANE_TABLET.get());
			}
		};
		ConfigHolder.init();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

		ModBlocks.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModItems.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		ProjectEXBlockEntities.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		ModMenuTypes.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
//		ModRecipeTypes.register();

		ModRecipeSerializers.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
		// ProjectEXMenus.REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());


		modBus.addListener(this::commonSetup);
		forgeBus.addListener(this::addReloadListeners);
		forgeBus.addListener(EMCSyncHandler.INSTANCE::onServerTick);
		//ProjectEXNet.init();
		//PROXY.init();
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		NetworkHandler.init();
	}

	private void addReloadListeners(AddReloadListenerEvent event) {
		event.addListener(RecipeCache.getCacheReloadListener());
	}

}
