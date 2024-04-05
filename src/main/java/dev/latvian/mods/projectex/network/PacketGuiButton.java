package dev.latvian.mods.projectex.network;


import dev.latvian.mods.projectex.menu.IGuiButtonListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.network.NetworkEvent;


import java.util.function.Supplier;

/**
 * Received on: SERVER
 * Sent by client GUI's when an EXButton (which has a tag) is clicked
 */
public class PacketGuiButton {
    private final String tag;
    private final boolean shift;

    public PacketGuiButton(String tag, boolean shift) {
        this.tag = tag;
        this.shift = shift;
    }
    
    public PacketGuiButton(FriendlyByteBuf buf) {
        this.tag = buf.readUtf(256);
        this.shift = buf.readBoolean();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(tag);
        buf.writeBoolean(shift);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        System.out.println("PacketGuiButton-----------------------------------------------------------------");
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null && player.containerMenu instanceof IGuiButtonListener) {
                IGuiButtonListener l = (IGuiButtonListener) player.containerMenu;
                l.handleGUIButtonPress(tag, shift, player);
            }
        });
        ctx.get().setPacketHandled(true);    
    }
}
