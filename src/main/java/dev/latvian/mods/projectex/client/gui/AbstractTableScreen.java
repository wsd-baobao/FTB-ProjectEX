package dev.latvian.mods.projectex.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.latvian.mods.projectex.block.entity.AbstractEMCBlockEntity;
import dev.latvian.mods.projectex.client.gui.buttons.ExtractItemButton;
import dev.latvian.mods.projectex.config.ConfigHelper;

import dev.latvian.mods.projectex.integration.jei.JEIHooks;
import dev.latvian.mods.projectex.menu.AbstractTableMenu;
import moze_intel.projecte.api.ItemInfo;
import moze_intel.projecte.api.ProjectEAPI;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public abstract class AbstractTableScreen<C extends AbstractTableMenu> extends AbstractEXScreen<C, AbstractEMCBlockEntity> implements KnowledgeUpdateListener {
    // static so they persist across GUI invocations
    private static int staticPage = 0;
    private static String staticSearch = "";

    private final List<ItemStack> validItems = new ArrayList<>();
    protected final List<ExtractItemButton> extractButtons = new ArrayList<>();
    private EditBox searchField;

    public AbstractTableScreen(C menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        imageHeight = 217;
    }

    @Override
    protected void init() {
        super.init();

        Rect2i tb = searchFieldPos();
        searchField = new EditBox(font, tb.getX(), tb.getY(), tb.getWidth(), tb.getHeight(), TextComponent.EMPTY);
        searchField.setTextColor(0xFFFFFFFF);
        searchField.setTextColorUneditable(0xFF808080);
        searchField.setBordered(false);
        searchField.setMaxLength(35);
        searchField.setValue(staticSearch);
//        if (ConfigHelper.client().general.searchType.get().autoselected) {
//            setInitialFocus(searchField);
//        }
        addButton(searchField);
    }

    protected abstract Rect2i searchFieldPos();

    protected void addExtractButton(ExtractItemButton extractItemButton) {
        extractButtons.add(extractItemButton);
        addButton(extractItemButton);
    }

    @Override
    public void tick() {

        super.tick();
        containerTick();
    }

    protected void containerTick() {
        if (!staticSearch.equals(searchField.getValue())) {
            staticSearch = searchField.getValue();
            staticPage = 0;
            updateValidItemList();
            if (ConfigHelper.client().general.searchType.get().jeiSync) JEIHooks.handleJEISync(staticSearch);
        }
    }


    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        changePage(delta < 0);  // scroll down = forward a page
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (searchFieldPos().contains((int) mouseX, (int) mouseY) && button == 1) {
            searchField.setValue("");
            return true;
        }
//        System.out.println(mouseY);
//        System.out.println(mouseX);
//        System.out.println(button);
        return super.mouseClicked(mouseX, mouseY, button);
    }



    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY).ifPresent(p -> {
                String s = EMCFormat.INSTANCE.format(p.getEmc());
                font.draw(poseStack, s, ((imageWidth - font.width(s)) / 2f),  -9f, 0xFFB5B5B5);
            });
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.minecraft.player.closeContainer();
        }
        if (this.searchField.keyPressed(keyCode, scanCode, modifiers) || this.searchField.canConsumeInput()) {
            if (keyCode == GLFW.GLFW_KEY_TAB) changeFocus(!Screen.hasShiftDown());
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onKnowledgeUpdate() {
        staticPage = 0;
        staticSearch = "";
        searchField.setValue("");
        updateValidItemList();
    }

    protected void changePage(boolean forward) {
        if (forward) {
            if (staticPage < Math.ceil(validItems.size() / (float) extractButtons.size()) - 1) {
                staticPage++;
                updateDisplayedItems();
            }
        } else if (staticPage > 0) {
            staticPage--;
            updateDisplayedItems();
        }
    }

    protected void updateValidItemList() {
        validItems.clear();

        String srchStr = trim(staticSearch);
        boolean mod = srchStr.startsWith("@");
        if (mod) {
            srchStr = srchStr.substring(1);
        }

        for (ItemInfo itemInfo : menu.getProvider().getKnowledge()) {
            ItemStack stack = itemInfo.createStack();
            if (!stack.isEmpty() && menu.isItemValid(stack) && (srchStr.isEmpty() || mod ?
                    Objects.requireNonNull(itemInfo.getItem().getRegistryName()).getNamespace().startsWith(srchStr) :
                    StringUtils.contains(trim(stack.getDisplayName().getString()), srchStr)))
            {
                validItems.add(ProjectEAPI.getEMCProxy().getPersistentInfo(itemInfo).createStack());
            }
        }

        validItems.sort(Comparator.comparingLong(o -> ProjectEAPI.getEMCProxy().getValue(o)));
//        Collections.reverse(validItems);
        updateDisplayedItems();
    }

    private String trim(String s) {
        return ChatFormatting.stripFormatting(s).trim().toLowerCase(Locale.ROOT);
    }

    public void updateDisplayedItems() {
        for (int i = 0; i < extractButtons.size(); i++) {
            int index = i + staticPage * extractButtons.size();
            if (index >= 0 && index < validItems.size()) {
                extractButtons.get(i).setItem(validItems.get(index));
                extractButtons.get(i).visible = true;
            } else {
                extractButtons.get(i).setItem(ItemStack.EMPTY);
                extractButtons.get(i).visible = false;
            }
        }
    }
}
