package dev.latvian.mods.projectex.integration.jei;

import dev.latvian.mods.projectex.ProjectEX;
import dev.latvian.mods.projectex.recipes.AlchemyTableRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class RecipeTypes {
    public static final RecipeType<AlchemyTableRecipe> ALCHEMY_TABLE = register("alchemy_table", AlchemyTableRecipe.class);

    private static <T extends Recipe<?>> RecipeType<T> register(String name, Class<T> recipeClass) {
        return RecipeType.register(name);
    }
}
