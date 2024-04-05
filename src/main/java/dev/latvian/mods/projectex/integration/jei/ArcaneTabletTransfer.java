package dev.latvian.mods.projectex.integration.jei;

import dev.latvian.mods.projectex.menu.ArcaneTabletMenu;
import dev.latvian.mods.projectex.network.NetworkHandler;
import dev.latvian.mods.projectex.network.PacketArcaneTabletRecipeTransfer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiIngredient;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ArcaneTabletTransfer implements IRecipeTransferHandler<ArcaneTabletMenu> {
    private final IRecipeTransferHandlerHelper transferHelper;

    public ArcaneTabletTransfer(IRecipeTransferHandlerHelper transferHelper) {
        this.transferHelper = transferHelper;
    }

    @Override
    public Class<ArcaneTabletMenu> getContainerClass() {
        return ArcaneTabletMenu.class;
    }


    public Class<CraftingRecipe> getRecipeClass() {
        return CraftingRecipe.class;
    }


    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(ArcaneTabletMenu container, Object recipe, IRecipeLayout recipeLayout, Player player, boolean maxTransfer, boolean doTransfer) {

        System.out.println(container);
        System.out.println(recipe);
        System.out.println(recipeLayout);
        System.out.println(player);
        System.out.println(maxTransfer);
        if (doTransfer) {




            NetworkHandler.sendToServer(new PacketArcaneTabletRecipeTransfer(null, maxTransfer));
        }

        return null;
    }
}
