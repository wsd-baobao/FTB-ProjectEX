package dev.latvian.mods.projectex.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockEntityTicker<T extends BlockEntity> {
    void tick(Level arg, BlockPos arg2, BlockState arg3, T arg4);
}
