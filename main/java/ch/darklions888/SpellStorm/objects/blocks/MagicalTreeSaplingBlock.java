package ch.darklions888.SpellStorm.objects.blocks;

import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.block.trees.Tree;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;

public class MagicalTreeSaplingBlock extends BushBlock implements IGrowable {
	public static final IntegerProperty STAGE = BlockStateProperties.STAGE_0_1;
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(2.0d, 0.0d, 2.0d, 14.0d, 12.0d, 14.0d);
	private final Supplier<Tree> tree;

	public MagicalTreeSaplingBlock(Supplier<Tree> treeIn, Properties properties) {
		super(properties);

		this.tree = treeIn;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
		super.tick(state, worldIn, pos, rand);

		if (!worldIn.isAreaLoaded(pos, 1)) {
			return;
		}
		if (worldIn.getLight(pos.up()) >= 9 && rand.nextInt(7) == 0) {
			this.grow(worldIn, pos, state, rand);
		}
	}

	public void grow(ServerWorld worldIn, BlockPos pos, BlockState state, Random rand) {
		if (state.get(STAGE) == 0) {
			worldIn.setBlockState(pos, state.func_235896_a_(STAGE), 4);
		} else {
			if (!ForgeEventFactory.saplingGrowTree(worldIn, rand, pos))
					return;

			this.tree.get().attemptGrowTree(worldIn, worldIn.getChunkProvider().getChunkGenerator(), pos, state, rand);

		}
	}

	@Override
	public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
		this.grow(worldIn, pos, state, rand);
	}

	@Override
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return (double) worldIn.rand.nextFloat() < 0.45D;
	}

	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(STAGE);
	}

}
