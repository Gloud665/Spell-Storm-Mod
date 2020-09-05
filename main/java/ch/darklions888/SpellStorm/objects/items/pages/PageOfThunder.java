package ch.darklions888.SpellStorm.objects.items.pages;

import java.util.List;

import ch.darklions888.SpellStorm.lib.MagicSource;
import ch.darklions888.SpellStorm.lib.ManaContainerSize;
import ch.darklions888.SpellStorm.lib.ManaPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class PageOfThunder extends BasePageItem
{
	
	public PageOfThunder(ManaContainerSize size, MagicSource source, ManaPower mana, int manaConsumption, TextFormatting format, boolean hasEffect, Properties properties) {
		super(size, source, mana, manaConsumption, format, hasEffect, properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) 
	{
		return getAbilities(worldIn, playerIn, handIn, playerIn.getHeldItem(handIn));
	}
	
	@Override
	public ActionResult<ItemStack> getAbilities(World worldIn, PlayerEntity playerIn, Hand handIn, ItemStack stack) {

		if (worldIn.isRemote) {
			return ActionResult.resultPass(stack);
			
		} else {
			
			if (playerIn.isCreative() || this.getMana(stack) > 0) {
				List<LivingEntity> entityList = worldIn.getEntitiesWithinAABB(LivingEntity.class,
						new AxisAlignedBB(playerIn.getPosX() - 10, playerIn.getPosY() - 10, playerIn.getPosZ() - 10,
								playerIn.getPosX() + 10, playerIn.getPosY() + 10, playerIn.getPosZ() + 10));

				ServerWorld serverworld = (ServerWorld) worldIn;
				serverworld.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.PLAYERS, 1.0f, 1.0f);
				
				if (entityList.size() > 1) {
					for (Entity entity : entityList) {
						if (entity != playerIn && entity instanceof LivingEntity) {
							LightningBoltEntity lighting = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, worldIn);
							lighting.setPosition(entity.getPosX(), entity.getPosY(), entity.getPosZ());
							serverworld.addEntity(lighting);
						}
					}
					
					if (!playerIn.isCreative())
						this.addMana(stack, -this.manaConsumption);
					return ActionResult.resultSuccess(stack);
				} else {
					return ActionResult.resultFail(stack);
				}
			} else {
				return ActionResult.resultFail(stack);
			}
		}
	}
}
