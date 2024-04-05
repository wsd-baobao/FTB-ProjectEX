package dev.latvian.mods.projectex.block;

import dev.latvian.mods.projectex.block.entity.AbstractEMCBlockEntity;
import dev.latvian.mods.projectex.block.entity.AbstractLinkBlockEntity;
import dev.latvian.mods.projectex.block.entity.BlockEntityTicker;
import dev.latvian.mods.projectex.block.entity.TickingEXBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public abstract class AbstractEXBlock extends BaseEntityBlock {
    public AbstractEXBlock() {
        super(Properties.of(Material.STONE).strength(5F).sound(SoundType.STONE));
    }

    public AbstractEXBlock(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            if (level.getBlockEntity(pos) instanceof AbstractEMCBlockEntity && level.getBlockEntity(pos) instanceof MenuProvider) {
                MenuProvider menuProvider = (MenuProvider) level.getBlockEntity(pos);
                AbstractEMCBlockEntity emc = (AbstractEMCBlockEntity) level.getBlockEntity(pos);
                if (emc instanceof AbstractLinkBlockEntity && !player.getUUID().equals(((AbstractLinkBlockEntity) emc).getOwnerId())) {
                    AbstractLinkBlockEntity link = (AbstractLinkBlockEntity) emc;
                    player.displayClientMessage(new TextComponent(link.getOwnerName()), true);
                    return InteractionResult.FAIL;
                }
                NetworkHooks.openGui(serverPlayer, menuProvider, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return (level1, blockPos, blockState, blockEntity) -> {
            if (blockEntity instanceof TickingEXBlockEntity) {
                TickingEXBlockEntity ticker = (TickingEXBlockEntity) blockEntity;
                ticker.tickCommonPre();
                if (level1.isClientSide()) {
                    ticker.tickClient();
                } else {
                    ticker.tickServer();
                }
                ticker.tickCommonPost();
            }
        };
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        if (entity != null) {
            if (level.getBlockEntity(pos) instanceof AbstractLinkBlockEntity) {
                AbstractLinkBlockEntity link = (AbstractLinkBlockEntity) level.getBlockEntity(pos);
                if (link != null) {
                    link.setOwner(entity);
                }
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (world.getBlockEntity(pos) instanceof AbstractEMCBlockEntity) {
                AbstractEMCBlockEntity invBe = (AbstractEMCBlockEntity) world.getBlockEntity(pos);
                if (invBe != null) {
                    Containers.dropContents(world, pos, invBe.getContentsToDrop());
                }
            }
        }

        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
}
