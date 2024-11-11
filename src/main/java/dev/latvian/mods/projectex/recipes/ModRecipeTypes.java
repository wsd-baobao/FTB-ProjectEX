package dev.latvian.mods.projectex.recipes;


import net.minecraft.world.item.crafting.RecipeType;


public class ModRecipeTypes {
    public static final RecipeType<AlchemyTableRecipe> ALCHEMY_TABLE = RecipeType.register("projectex:alchemy_table");

    public static void register() {

        // 这里不需要显式注册到事件总线，但可以在需要引用的地方确保配方类型已经加载
    }

//    public static final DeferredRegister<RecipeType<?>> REGISTRY = DeferredRegister.create(, ProjectEX.MOD_ID);
//
//    public static final RegistryObject<RecipeType<AlchemyTableRecipe>> ALCHEMY_TABLE = REGISTRY.register("alchemy_table", AlchemyTableRecipeType::new);
//
//    public static class AlchemyTableRecipeType implements RecipeType<AlchemyTableRecipe> {
//    }
}
