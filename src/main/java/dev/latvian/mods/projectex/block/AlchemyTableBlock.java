package dev.latvian.mods.projectex.block;

import dev.latvian.mods.projectex.block.entity.AlchemyTableEntity;
import dev.latvian.mods.projectex.block.entity.ProjectEXBlockEntities;
import dev.latvian.mods.projectex.menu.AlchemyTableMenu;
import dev.latvian.mods.projectex.menu.ModMenuTypes;
import dev.latvian.mods.projectex.recipes.ModRecipeTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class AlchemyTableBlock extends AbstractEXBlock {
	public static final VoxelShape SHAPE = Shapes.or(
			box(0, 9, 0, 16, 13, 16),
			box(2, 0, 2, 4, 9, 4),
			box(2, 0, 12, 4, 9, 14),
			box(12, 0, 12, 14, 9, 14),
			box(12, 0, 2, 14, 9, 4)
	);

	public AlchemyTableBlock() {
		super(Properties.of(Material.STONE).strength(1F).sound(SoundType.STONE).noOcclusion());
	}



	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
		double x = pos.getX();
		double y = pos.getY() + 1.15D;
		double z = pos.getZ();

		level.addParticle(ParticleTypes.FLAME, x + 2.5D / 16D, y, z + 2.5D / 16D, 0D, 0D, 0D);
		level.addParticle(ParticleTypes.FLAME, x + 13.5D / 16D, y, z + 13.5D / 16D, 0D, 0D, 0D);
		level.addParticle(ParticleTypes.FLAME, x + 13.5D / 16D, y, z + 2.5D / 16D, 0D, 0D, 0D);
		level.addParticle(ParticleTypes.FLAME, x + 2.5D / 16D, y, z + 13.5D / 16D, 0D, 0D, 0D);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockGetter arg) {
		return new AlchemyTableEntity();
	}
}
