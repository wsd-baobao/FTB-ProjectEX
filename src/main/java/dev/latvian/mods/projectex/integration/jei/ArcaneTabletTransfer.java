package dev.latvian.mods.projectex.integration.jei;

import dev.latvian.mods.projectex.menu.ArcaneTabletMenu;
import dev.latvian.mods.projectex.network.NetworkHandler;
import dev.latvian.mods.projectex.network.PacketArcaneTabletRecipeTransfer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiIngredient;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.*;
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
        if (doTransfer) {
            if (recipe instanceof CraftingRecipe){



            Map<Integer, ? extends IGuiIngredient<ItemStack>> guiIngredients = recipeLayout.getItemStacks().getGuiIngredients();
            Map<Integer, ItemStack> temp = new HashMap<>();
            for (int i = 0; i < guiIngredients.size()-1; i++){
                if (guiIngredients.get(i+1).getDisplayedIngredient()==null){
                    temp.put(i, ItemStack.EMPTY);
                }else {
                    temp.put(i, guiIngredients.get(i+1).getDisplayedIngredient());
                }
            }
            List<Slot> slots = container.slots
                    .stream().filter(s -> s.container instanceof CraftingContainer)
                    .collect(Collectors.toList());
            System.out.println(slots);
            System.out.println(temp);
            if (slots.size() != temp.size()){
                return transferHelper.createInternalError();
            }
            Int2ObjectMap<List<ItemStack>> stacksMap = new Int2ObjectOpenHashMap<>();
            for (int i = 0; i < temp.size(); i++){
                stacksMap.put(i, Collections.singletonList((temp.get(i))));
            }


            NetworkHandler.sendToServer(new PacketArcaneTabletRecipeTransfer(stacksMap, maxTransfer));
        }
        }

        return null;
    }
}
