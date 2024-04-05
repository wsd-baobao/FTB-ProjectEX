package dev.latvian.mods.projectex.network;


import dev.latvian.mods.projectex.client.ClientUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;


import java.util.function.Supplier;

/**
 * Received on: CLIENT
 * Sent by server when knowledge is changed, to force any open GUI to refresh itself
 */
public class PacketNotifyKnowledgeChange {
    public PacketNotifyKnowledgeChange() {
    }

    public PacketNotifyKnowledgeChange(FriendlyByteBuf buf) {
    }

    void toBytes(FriendlyByteBuf buf) {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() == null) {
                ClientUtils.onknowledgeUpdate();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
