package dev.latvian.mods.projectex.inventor;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

public class BaseItemStackHandler<T extends BlockEntity> extends ItemStackHandler {
    protected final T owningBlockEntity;

    public BaseItemStackHandler(T owningBlockEntity, int size) {
        super(size);
        this.owningBlockEntity = owningBlockEntity;
    }
    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);

        owningBlockEntity.setChanged();
    }
}
