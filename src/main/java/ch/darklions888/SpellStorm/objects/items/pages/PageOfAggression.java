package ch.darklions888.SpellStorm.objects.items.pages;

import java.util.List;

import ch.darklions888.SpellStorm.lib.MagicSource;
import ch.darklions888.SpellStorm.lib.ManaContainerSize;
import ch.darklions888.SpellStorm.lib.ManaPower;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class PageOfAggression extends AbstractPageItem {

	private static final String MOB_TAG = "spellstrom_aggressive_mob_tag";

	public PageOfAggression(Properties properties) {
		super(ManaContainerSize.MEDIUM, MagicSource.DARKMAGIC, ManaPower.MEDIUM, 1, TextFormatting.DARK_RED, true, properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		return this.getAbilities(worldIn, playerIn, handIn, playerIn.getHeldItem(handIn), null);
	}

	@Override
	public ActionResult<ItemStack> getAbilities(World worldIn, PlayerEntity playerIn, Hand handIn, ItemStack stackIn, ItemStack bookIn) {

		if (worldIn.isRemote) {

			return ActionResult.resultPass(stackIn);

		} else {

			ServerWorld serverWorld = (ServerWorld) worldIn;

			if (playerIn.isCreative() || this.getMana(stackIn) >= this.manaConsumption) {
				List<MobEntity> entityList;
				double x = playerIn.getPosX();
				double y = playerIn.getPosY();
				double z = playerIn.getPosZ();

				entityList = serverWorld.getEntitiesWithinAABB(MobEntity.class,	new AxisAlignedBB(x + 7, y + 5, z + 7, x - 7, y - 2, z - 7));
				
				for (MobEntity e : entityList) {
					System.out.println(e.getAttributeManager().hasAttributeInstance(Attributes.ATTACK_DAMAGE));
					if (!e.getTags().contains(MOB_TAG) && e.getAttributeManager().hasAttributeInstance(Attributes.ATTACK_DAMAGE)) {

						for (int i = 0; i < 4; i++) {
							((ServerWorld) worldIn).spawnParticle(ParticleTypes.ANGRY_VILLAGER, e.getPosXRandom(0.6d), e.getPosYRandom(), e.getPosZRandom(0.6d), 1, 0, 0, 0, 1.0d);
						}
						e.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(e, MobEntity.class, true, true));
						e.addTag(MOB_TAG);
						/*
						if (e.getAttribute(Attributes.ATTACK_DAMAGE) == null || !(e instanceof SlimeEntity)) {
							try {
								Multimap<Attribute, AttributeModifier> attriMap = HashMultimap.create();
							     Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
							     builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Spell modifier", (double)e.getHealth()/3, AttributeModifier.Operation.ADDITION));
							     builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Spell modifier", (double)1.0d, AttributeModifier.Operation.ADDITION));
							     attriMap = builder.build();
							     e.getAttributeManager().reapplyModifiers(attriMap);

								e.targetSelector.addGoal(0, new MeleeAttackGoal((CreatureEntity)e, 2.0d, false));
							} catch (Exception exception) {exception.printStackTrace();}
						}
						*/
						if (!playerIn.isCreative())
							this.addMana(stackIn, -this.manaConsumption);
					}
					
					//serverWorld.playSound(null, x, y, z, SoundEvents.ENTITY_ENDER_EYE_DEATH, SoundCategory.PLAYERS, 1.0f, 1.0f);
				}


				if (entityList.size() > 0) {
					return ActionResult.resultSuccess(stackIn);
				} else {
					return ActionResult.resultPass(stackIn);
				}
			} else {
				return ActionResult.resultPass(stackIn);
			}
		}
	}

	@Override
	public int getInkColor() {
		return 0x8d3535;
	}
}
