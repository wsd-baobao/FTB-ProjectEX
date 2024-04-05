package dev.latvian.mods.projectex.block.entity;

import dev.latvian.mods.projectex.ProjectEX;
import dev.latvian.mods.projectex.block.CollectorBlock;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.tile.IEmcStorage;
import moze_intel.projecte.gameObjs.tiles.RelayMK1Tile;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CollectorBlockEntity extends BlockEntity implements TickableBlockEntity, IEmcStorage {
	public int tick = 0;
	public long storedEMC = 0L;
	private LazyOptional<IEmcStorage> emcStorageCapability;

	public CollectorBlockEntity() {
		super(ProjectEXBlockEntities.COLLECTOR.get());
	}

	@Override
	public void load(BlockState state, CompoundTag tag) {
		super.load(state, tag);
		tick = tag.getByte("Tick") & 0xFF;
		storedEMC = tag.getLong("StoredEMC");
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		super.save(tag);
		tag.putByte("Tick", (byte) tick);
		tag.putLong("StoredEMC", storedEMC);
		return tag;
	}

	@Override
	public void onLoad() {
		if (level != null && level.isClientSide()) {
			level.tickableBlockEntities.remove(this);
		}

		super.onLoad();
	}

	@Override
	public void tick() {
		if (level != null && level.isClientSide()) {
			return;
		}

		tick++;

		if (tick >= 20) {
			tick = 0;

			BlockState state = getBlockState();

			if (state.getBlock() instanceof CollectorBlock) {
				storedEMC += ((CollectorBlock) state.getBlock()).matter.collectorOutput;

				List<IEmcStorage> temp = new ArrayList<>(1);

				for (Direction direction : ProjectEX.DIRECTIONS) {
					BlockEntity blockEntity = level.getBlockEntity(worldPosition.relative(direction));
					IEmcStorage storage = blockEntity == null ? null : blockEntity.getCapability(ProjectEAPI.EMC_STORAGE_CAPABILITY, direction.getOpposite()).orElse(null);

					if (storage != null && storage.insertEmc(1L, IEmcStorage.EmcAction.SIMULATE) > 0L) {
						temp.add(storage);

						if (blockEntity instanceof RelayMK1Tile) {
							for (int i = 0; i < 20; i++) {
								((RelayMK1Tile) blockEntity).addBonus();
							}

							blockEntity.setChanged();
						} else if (blockEntity instanceof RelayBlockEntity) {
							((RelayBlockEntity) blockEntity).addBonus();
							blockEntity.setChanged();
						}
					}
				}

				if (!temp.isEmpty() && storedEMC >= temp.size()) {
					long s = storedEMC / temp.size();

					for (IEmcStorage storage : temp) {
						long a = storage.insertEmc(s, EmcAction.EXECUTE);

						if (a > 0L) {
							storedEMC -= a;
							setChanged();

							if (storedEMC < s) {
								break;
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void setChanged() {
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
		long e = Math.min(storedEMC, emc);

		if (e < 0L) {
			return insertEmc(-e, action);
		} else if (action.execute()) {
			storedEMC -= e;
		}

		return e;
	}

	@Override
	public long insertEmc(long l, EmcAction emcAction) {
		return 0L;
	}

	@Override
	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == ProjectEAPI.EMC_STORAGE_CAPABILITY) {
			if (emcStorageCapability == null || !emcStorageCapability.isPresent()) {
				emcStorageCapability = LazyOptional.of(() -> this);
			}

			return emcStorageCapability.cast();
		}

		return super.getCapability(cap, side);
	}

	@Override
	protected void invalidateCaps() {
		super.invalidateCaps();

		if (emcStorageCapability != null && emcStorageCapability.isPresent()) {
			emcStorageCapability.invalidate();
			emcStorageCapability = null;
		}
	}
}
