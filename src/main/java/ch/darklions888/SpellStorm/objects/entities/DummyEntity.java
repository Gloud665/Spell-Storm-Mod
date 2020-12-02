
package ch.darklions888.SpellStorm.objects.entities;

import java.util.EnumSet;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.Goal.Flag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class DummyEntity extends MobEntity{
	
	private Goal tap = new Goal() {

		@Override
		public boolean shouldContinueExecuting() {
			
			return false;
		}

		@Override
		public boolean isPreemptible() {
			LOGGER.info("isPreemptible");
			return super.isPreemptible();
		}

		@Override
		public void resetTask() {
			LOGGER.info("resetTask");
			super.resetTask();
		}

	

		@Override
		public void startExecuting() {
			super.startExecuting();
			DummyEntity.this.jump();
			LOGGER.info("я прыгаю");
		}

		@Override
		public boolean shouldExecute() {
			// TODO Auto-generated method stub
			return DummyEntity.this.isOnGround();
		}
		
	};
	private Goal scream = new Goal() {

		@Override
		public boolean shouldContinueExecuting() {
			// TODO Auto-generated method stub
			return super.shouldContinueExecuting();
		}

		@Override
		public void startExecuting() {
			DummyEntity.this.move(MoverType.SELF, new Vector3d(1, 1, 0));;
			super.startExecuting();
			DummyEntity.this.attacked=false;
			LOGGER.info("я типа убегаю");
		}

		@Override
		public boolean shouldExecute() {
			
			return DummyEntity.this.attacked;
		}
		
	};
	
	boolean attacked = false;
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		// TODO Auto-generated method stub
		this.attacked = true;
		LOGGER.info("меня ударили!");
		return super.attackEntityFrom(source, amount);
	}

	public DummyEntity(EntityType<? extends MobEntity> type, World worldIn) {
		super(type, worldIn);
		this.goalSelector.addGoal(2, tap );
		this.goalSelector.addGoal(1, scream );
		
	}
	
	public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 10.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D);
    }
}