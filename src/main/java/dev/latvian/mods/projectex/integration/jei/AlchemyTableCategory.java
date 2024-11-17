package dev.latvian.mods.projectex.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.latvian.mods.projectex.ProjectEX;
import dev.latvian.mods.projectex.block.ModBlocks;
import dev.latvian.mods.projectex.client.gui.EMCFormat;
import dev.latvian.mods.projectex.recipes.AlchemyTableRecipe;
import dev.latvian.mods.projectex.util.EXUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class AlchemyTableCategory implements IRecipeCategory<AlchemyTableRecipe> {

    public static final ResourceLocation ID = new ResourceLocation(ProjectEX.MOD_ID, "alchemy_table");
    private IDrawable background;
    private IDrawable iron;

    public AlchemyTableCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(EXUtils.rl("textures/gui/alchemy_table_jei.png"), 0, 0, 128, 18)
                .setTextureSize(128, 64).build();
        this.iron = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.ALCHEMY_TABLE.get()));
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return iron;
    }

    @Override
    public Class<? extends AlchemyTableRecipe> getRecipeClass() {
        return AlchemyTableRecipe.class;
    }

    @Override
    public String getTitle() {
        return "ProjectEX: Alchemy Table";
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }

    @Override
    public void setIngredients(AlchemyTableRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AlchemyTableRecipe recipe, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 0, 0);
        recipeLayout.getItemStacks().init(1, false, 110, 0);

        recipeLayout.getItemStacks().set(ingredients);
    }

    @Override
    public void draw(AlchemyTableRecipe recipe, PoseStack matrixStack, double mouseX, double mouseY) {
        net.minecraft.client.gui.Font font = Minecraft.getInstance().font;
        // 获取配方当中的输入的物品
        ItemStack[] inputStack = recipe.getIngredients().get(0).getItems();

        long emc = recipe.getTotalCost(inputStack[0]);
        String s = EMCFormat.INSTANCE.format(emc) + " EMC";
        font.draw(matrixStack, s, (128 - font.width(s)) / 2f, 5f, 0xFF404040);

    }

}
