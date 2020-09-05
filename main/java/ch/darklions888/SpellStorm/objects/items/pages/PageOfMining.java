package ch.darklions888.SpellStorm.objects.items.pages;

import ch.darklions888.SpellStorm.lib.MagicSource;
import ch.darklions888.SpellStorm.lib.ManaContainerSize;
import ch.darklions888.SpellStorm.lib.ManaPower;
import ch.darklions888.SpellStorm.util.helpers.mathhelpers.CalculateLineCoordinates;
import ch.darklions888.SpellStorm.util.helpers.mathhelpers.RayTraceHelper;
import ch.darklions888.SpellStorm.util.helpers.mathhelpers.Vec3;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class PageOfMining extends BasePageItem {


	public PageOfMining(ManaContainerSize size, MagicSource source, ManaPower mana, int manaConsumption, TextFormatting format, boolean hasEffect, Properties properties) {
		super(size, source, mana, manaConsumption, format, hasEffect, properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		return getAbilities(worldIn, playerIn, handIn, playerIn.getHeldItem(handIn));
	}
	
	@Override
	public ActionResult<ItemStack> getAbilities(World worldIn, PlayerEntity playerIn, Hand handIn, ItemStack stackIn) {
		
		if(worldIn.isRemote) {
			return ActionResult.resultPass(stackIn);
		}else {
			
			if(playerIn.isCreative() || this.getMana(stackIn) > 0) {
				
				ServerWorld serverWorld = (ServerWorld) worldIn;
				
				BlockRayTraceResult  result = (BlockRayTraceResult)RayTraceHelper.CustomrayTrace(worldIn, playerIn, FluidMode.NONE, 10d);
				
				if (result != null && result.getType() == RayTraceResult.Type.BLOCK) {
					BlockPos pos = result.getPos();
					BlockState state = serverWorld.getBlockState(pos);
					
					if (state.getBlockHardness(serverWorld, pos)  >= 0) {
						FluidState ifluidstate = serverWorld.getFluidState(pos);
						Block.spawnDrops(state.getBlockState(), worldIn, pos);
						serverWorld.setBlockState(pos, ifluidstate.getBlockState(), 3);
						
						CalculateLineCoordinates line = new CalculateLineCoordinates(new Vec3(playerIn.getPosX(), playerIn.getPosYEye(), playerIn.getPosZ()), new Vec3(pos.getX(), pos.getY(), pos.getZ()));
						for (Vec3 v : line.CoordListVec3()) {
							serverWorld.spawnParticle(ParticleTypes.END_ROD, v.X(), v.Y(), v.Z(), 1, 0, 0, 0, 0.5f);
						}
						
						if (!playerIn.isCreative())
							this.addMana(stackIn, -this.manaConsumption);
					} else {
						return ActionResult.resultPass(stackIn);
					}
				}
				return ActionResult.resultSuccess(stackIn);
			}else {
				return ActionResult.resultPass(stackIn);
			}

		}
		
	}
	
}
