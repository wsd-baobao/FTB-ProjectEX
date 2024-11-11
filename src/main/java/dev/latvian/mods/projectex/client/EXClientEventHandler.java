package dev.latvian.mods.projectex.client;

import com.google.common.collect.EvictingQueue;
import dev.latvian.mods.projectex.ProjectEX;
import dev.latvian.mods.projectex.client.gui.EMCFormat;
import dev.latvian.mods.projectex.config.ConfigHelper;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

@Mod.EventBusSubscriber(modid = ProjectEX.MOD_ID, value = Dist.CLIENT)
public class EXClientEventHandler {
    private static int timer;
    private static BigInteger emcAmount;  // tracks current player EMC level
    private static BigInteger lastEMC = BigInteger.ZERO;
    private static final int RING_BUFFER_SIZE = 5;
    @SuppressWarnings("UnstableApiUsage")
    private static final EvictingQueue<BigInteger> emcRingBuffer = EvictingQueue.create(RING_BUFFER_SIZE);
    private static final BigInteger bufferSize = BigInteger.valueOf(RING_BUFFER_SIZE);

    public static BigInteger emcRate = BigInteger.ZERO;  // rate of change of player EMC over last 5 seconds

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getInstance().player != null) {
            // calculate change rate in player's personal EMC using a 5-second running average
            if (++timer == 20) {
                emcAmount = Minecraft.getInstance().player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY)
                        .map(IKnowledgeProvider::getEmc).orElse(BigInteger.ZERO);
                emcRingBuffer.add(emcAmount.subtract(lastEMC));
                lastEMC = emcAmount;

                emcRate = BigInteger.ZERO;
                for (BigInteger d : emcRingBuffer) {
                    emcRate = emcRate.add(d);
                }
                emcRate = emcRate.divide(bufferSize);

                timer = 0;
            }
        }
    }

    @SubscribeEvent
    public static void onClientDisconnect(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        emcAmount = BigInteger.ZERO;
        timer = 0;
        emcRate = BigInteger.ZERO;
        emcRingBuffer.clear();
    }

    @SubscribeEvent
    public static void addInfoText(RenderGameOverlayEvent.Text event) {
        if (Minecraft.getInstance().player != null &&
                (!ConfigHelper.client().general.onlyShowEMCWhenHoldingModItem.get() || holdingValidItem(Minecraft.getInstance().player)))
        {
            EMCOverlayPosition oPos = ConfigHelper.client().general.screenPosition.get();
            if (oPos != EMCOverlayPosition.DISABLED && emcAmount.compareTo(BigInteger.ZERO) > 0) {
                (oPos == EMCOverlayPosition.TOP_LEFT ? event.getLeft() : event.getRight()).add("EMC: " + getEMCRateString());
            }
        }
    }

    @NotNull
    public static String getEMCRateString() {
        String s = EMCFormat.INSTANCE.format(emcAmount);
        if (emcRate.signum() != 0) {
            s += (emcRate.signum() > 0 ? (ChatFormatting.GREEN + "+") : (ChatFormatting.RED + "-")) + EMCFormat.INSTANCE.format(emcRate.abs()) + "/s";
        }
        return s;
    }

    private static boolean holdingValidItem(Player player) {
        return holdingValidItem(player.getMainHandItem()) || holdingValidItem(player.getOffhandItem());
    }
    private static boolean holdingValidItem(ItemStack stack) {
        String namespace = stack.getItem().getRegistryName().getNamespace();
        return namespace.equals(ProjectEX.MOD_ID) || namespace.equals(ProjectEAPI.PROJECTE_MODID);
    }
}
