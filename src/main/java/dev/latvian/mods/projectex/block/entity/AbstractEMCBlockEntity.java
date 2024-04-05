package dev.latvian.mods.projectex.block.entity;

import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.tile.IEmcStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public abstract class AbstractEMCBlockEntity extends BlockEntity implements TickingEXBlockEntity, IEmcStorage {
    protected long storedEMC = 0L;  // buffered in the block and moved to the player every 20 ticks
    private final LazyOptional<IEmcStorage> emcStorageCapability;

    public AbstractEMCBlockEntity(BlockEntityType<?> type) {
        super(type);
        emcStorageCapability = LazyOptional.of(() -> this);
    }

    @Override
    public void load(BlockState state,CompoundTag tag) {
        super.load(state,tag);
        storedEMC = tag.getLong("StoredEMC");

    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        super.save(tag);
        tag.putLong("StoredEMC", storedEMC);
        return tag;
    }

    @Override
    public void setChanged() {
        // overridden for efficiency, since these blocks don't have a comparator output
        if (level != null) {
            level.blockEntityChanged(worldPosition, this);
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

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ProjectEAPI.EMC_STORAGE_CAPABILITY) {
            return emcStorageCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();

        emcStorageCapability.invalidate();
    }

    @Nonnull
    Level nonNullLevel() {
        // for use where we *know* the level is non-null... keeps the IDE quiet
        return Objects.requireNonNull(getLevel());
    }

    /**
     * Get a list of the items to be dropped from the broken block's inventory. Override in subclasses.
     * Note: not the block itself, just its inventory!
     *
     * @return list of items
     */
    public NonNullList<ItemStack> getContentsToDrop() {
        return NonNullList.create();
    }
}
