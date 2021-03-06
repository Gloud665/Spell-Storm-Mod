package ch.darklions888.SpellStorm.objects.blocks;

import java.util.Random;
import java.util.stream.Stream;

import ch.darklions888.SpellStorm.objects.tileentities.MagicalForgeTileEntity;
import ch.darklions888.SpellStorm.registries.SoundInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class MagicalForgeBlock extends ContainerBlock {
	
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final BooleanProperty ON = BooleanProperty.create("on");

	private final static VoxelShape SHAPE_S = Stream.of(
			Block.makeCuboidShape(0, 0, 0, 16, 8, 16),
			Block.makeCuboidShape(0, 8, 5, 5, 15, 16),
			Block.makeCuboidShape(11, 8, 5, 16, 15, 16),
			Block.makeCuboidShape(0, 8, 0, 16, 15, 5),
			Block.makeCuboidShape(0, 15, 0, 16, 16, 16),
			Block.makeCuboidShape(5, 8, 15, 11, 15, 16),
			Block.makeCuboidShape(5, 8, 5, 11, 13, 15)
			).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
	
	private static final VoxelShape SHAPE_W = Stream.of(
			Block.makeCuboidShape(0, 0, 0, 16, 8, 16),
			Block.makeCuboidShape(5, 8, 11, 16, 15, 16),
			Block.makeCuboidShape(5, 8, 0, 16, 15, 5),
			Block.makeCuboidShape(0, 8, 0, 5, 15, 16),
			Block.makeCuboidShape(0, 15, 0, 16, 16, 16),
			Block.makeCuboidShape(15, 8, 5, 16, 15, 11),
			Block.makeCuboidShape(5, 8, 5, 15, 13, 11)
			).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
	
	private final static VoxelShape SHAPE_N = Stream.of(
			Block.makeCuboidShape(0, 0, 0, 16, 8, 16),
			Block.makeCuboidShape(11, 8, 0, 16, 15, 11),
			Block.makeCuboidShape(0, 8, 0, 5, 15, 11),
			Block.makeCuboidShape(0, 8, 11, 16, 15, 16),
			Block.makeCuboidShape(0, 15, 0, 16, 16, 16),
			Block.makeCuboidShape(5, 8, 0, 11, 15, 1),
			Block.makeCuboidShape(5, 8, 1, 11, 13, 11)
			).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
	
	private final static VoxelShape SHAPE_E = Stream.of(
			Block.makeCuboidShape(0, 0, 0, 16, 8, 16),
			Block.makeCuboidShape(0, 8, 0, 11, 15, 5),
			Block.makeCuboidShape(0, 8, 11, 11, 15, 16),
			Block.makeCuboidShape(11, 8, 0, 16, 15, 16),
			Block.makeCuboidShape(0, 15, 0, 16, 16, 16),
			Block.makeCuboidShape(0, 8, 5, 1, 15, 11),
			Block.makeCuboidShape(1, 8, 5, 11, 13, 11)
			).reduce((v1, v2) -> {return VoxelShapes.combineAndSimplify(v1, v2, IBooleanFunction.OR);}).get();
	
	public MagicalForgeBlock(Properties properties) {
		super(properties);
	      this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(ON, Boolean.valueOf(false)));
	}
	
	@Deprecated
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch (state.get(FACING)) {
		case NORTH: 
			return SHAPE_N;
		case SOUTH:
			return SHAPE_S;
		case WEST:
			return SHAPE_W;
		case EAST:
			return SHAPE_E;
		default:
			return SHAPE_N;
		}
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (worldIn.isRemote()) {
			return ActionResultType.SUCCESS;
		} else {
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof MagicalForgeTileEntity) {
				player.openContainer((INamedContainerProvider)tile);
			}
			
			return ActionResultType.CONSUME;
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}
	
	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}
	
	@Deprecated
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(FACING, ON);
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new MagicalForgeTileEntity();
	}
	
	@Deprecated
	@Override
	public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.isIn(newState.getBlock())) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof MagicalForgeTileEntity) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (MagicalForgeTileEntity) tileentity);
				((MagicalForgeTileEntity) tileentity).getIRecipeList(worldIn, Vector3d.copyCentered(pos));
				worldIn.updateComparatorOutputLevel(pos, this);
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}
	
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		if (stateIn.get(ON) && rand.nextInt(30) == 0) {
			worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D,
					SoundInit.MAGICAL_FURNACE_CRACKLES.get(), SoundCategory.BLOCKS, 1.5F, rand.nextFloat() * 0.4F + 0.4F, true);
			
	        Direction direction = stateIn.get(FACING);
	        Direction.Axis direction$axis = direction.getAxis();
	        double d0 = (double)pos.getX() + 0.5D;
	        double d1 = (double)pos.getY();
	        double d2 = (double)pos.getZ() + 0.5D;
	        //double d3 = 0.52D;
	        double d4 = rand.nextDouble() * 0.6D - 0.3D;
	        double d5 = direction$axis == Direction.Axis.X ? (double)direction.getXOffset() * 0.52D : d4;
	        double d6 = rand.nextDouble() * 9.0D / 16.0D;
	        double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getZOffset() * 0.52D : d4;
	        worldIn.addParticle(ParticleTypes.LANDING_LAVA, d0 + d5, d1 + d6, d2 + d7, 0.0D, 0.0D, 0.0D);
		}
	}
	
	@Deprecated
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
}
