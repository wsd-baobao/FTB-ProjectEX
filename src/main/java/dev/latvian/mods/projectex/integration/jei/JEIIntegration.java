package dev.latvian.mods.projectex.integration.jei;

import com.google.common.collect.ImmutableList;
import dev.latvian.mods.projectex.ProjectEX;
import dev.latvian.mods.projectex.block.ModBlocks;
import dev.latvian.mods.projectex.client.gui.AbstractEXScreen;
import dev.latvian.mods.projectex.client.gui.AlchemyTableScreen;
import dev.latvian.mods.projectex.client.gui.ArcaneTabletScreen;
import dev.latvian.mods.projectex.item.ModItems;
import dev.latvian.mods.projectex.recipes.ModRecipeTypes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;

@JeiPlugin
public class JEIIntegration implements IModPlugin {
    private static final ResourceLocation ID = new ResourceLocation(ProjectEX.MOD_ID, "default");

    static IJeiHelpers jeiHelpers;
    static IJeiRuntime runtime;

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        jeiHelpers = registry.getJeiHelpers();
    }


    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ALCHEMY_TABLE.get()), (ResourceLocation) RecipeTypes.ALCHEMY_TABLE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.ARCANE_TABLET.get()), mezz.jei.api.constants.VanillaRecipeCategoryUid.CRAFTING);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(AlchemyTableScreen.class, 78, 35, 23, 14, (ResourceLocation) RecipeTypes.ALCHEMY_TABLE);

        registration.addGhostIngredientHandler(AbstractEXScreen.class, new EMCLinkJEI<>());

        registration.addGuiContainerHandler(ArcaneTabletScreen.class, new ArcaneTabletGuiArea());
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addUniversalRecipeTransferHandler(new ArcaneTabletTransfer(registration.getTransferHelper()));
    }



    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
    }

    public static void setFilterText(String text) {
        runtime.getIngredientFilter().setFilterText(text);
    }
}
