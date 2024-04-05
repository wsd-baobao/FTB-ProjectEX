package dev.latvian.mods.projectex.recipes;


import dev.latvian.mods.projectex.ProjectEX;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ProjectEX.MOD_ID);

    public static final RegistryObject<RecipeSerializer<AlchemyTableRecipe>> ALCHEMY_TABLE
            = REGISTRY.register("alchemy_table", () -> new AlchemyTableRecipe.Serializer<>(AlchemyTableRecipe::new));
}
