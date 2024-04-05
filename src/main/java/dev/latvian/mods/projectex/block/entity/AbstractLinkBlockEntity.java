package dev.latvian.mods.projectex.block.entity;

import dev.latvian.mods.projectex.EMCSyncHandler;
import moze_intel.projecte.api.ProjectEAPI;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.math.BigInteger;
import java.util.UUID;

public class AbstractLinkBlockEntity extends AbstractEMCBlockEntity{
    private UUID ownerId = Util.NIL_UUID;
    private String ownerName = "";

    public AbstractLinkBlockEntity(BlockEntityType<?> type) {
        super(type);
    }

    @Override
    public void load(BlockState state,CompoundTag tag) {
        super.load(state,tag);
        ownerId = tag.getUUID("Owner");
        ownerName = tag.getString("OwnerName");
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        super.save(tag);
        tag.putUUID("Owner", ownerId);
        tag.putString("OwnerName", ownerName);
        return tag;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString("OwnerName", ownerName);
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if (pkt.getTag() != null) handleUpdateTag(null,pkt.getTag());
    }


    @Override
    public void handleUpdateTag(BlockState state, CompoundTag tag) {
        ownerName = tag.getString("OwnerName");
    }

    @Override
    public void tickServer() {
        if (nonNullLevel().getGameTime() % 20 == 0) {
            // move any locally stored EMC in the block into the player's network, if they're online
            if (storedEMC > 0L) {
                ServerPlayer player = nonNullLevel().getServer().getPlayerList().getPlayer(ownerId);
                if (player != null) {
                    player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY).ifPresent(provider -> {
                        provider.setEmc(provider.getEmc().add(BigInteger.valueOf(storedEMC)));
                        storedEMC = 0L;
                        setChanged();
                        EMCSyncHandler.INSTANCE.needsSync(player);
                    });
                }
            }
        }
    }

    @Override
    public void setChanged() {
        // overridden for efficiency, since link blocks don't have comparator output
        if (level != null) {
            level.blockEntityChanged(worldPosition,this);
        }
    }

    @Override
    public long getStoredEmc() {
        return storedEMC;
    }

    @Override
    public long getMaximumEmc() {
        return Long.MAX_VALUE;
    }

    @Override
    public long extractEmc(long emc, EmcAction action) {
        long toExtract = Math.min(storedEMC, emc);

        if (toExtract > 0L) {
            storedEMC -= toExtract;
            setChanged();
        }

        return toExtract;
    }

    @Override
    public long insertEmc(long emc, EmcAction action) {
        long toInsert = Math.min(getMaximumEmc() - storedEMC, emc);

        if (toInsert > 0L && action.execute()) {
            storedEMC += toInsert;
            setChanged();
        }

        return toInsert;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwner(LivingEntity entity) {
        ownerId = entity.getUUID();
        ownerName = entity.getScoreboardName();
    }

    public void trySyncEMC() {
        EMCSyncHandler.INSTANCE.needsSync(getOwnerId());
    }
}
