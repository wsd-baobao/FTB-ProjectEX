package dev.latvian.mods.projectex.recipes;


import dev.latvian.mods.projectex.ProjectEX;
import net.java.games.input.Component;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.*;


public class ModRecipeTypes {

    public static final RecipeType<AlchemyTableRecipe> ALCHEMY_TABLE = RecipeType.register("projectex:alchemy_table");


    public static <T extends Recipe<?>> RecipeType<T> register(String name, Class<T> recipeClass) {
        return RecipeType.register( name);
    }


//    public static final DeferredRegister<RecipeType<?>> REGISTRY = DeferredRegister.create(, ProjectEX.MOD_ID);
//
//    public static final RegistryObject<RecipeType<AlchemyTableRecipe>> ALCHEMY_TABLE = REGISTRY.register("alchemy_table", AlchemyTableRecipeType::new);
//
//    public static class AlchemyTableRecipeType implements RecipeType<AlchemyTableRecipe> {
//    }
}
