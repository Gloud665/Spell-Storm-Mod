package ch.darklions888.SpellStorm.registries;

import java.util.function.BiFunction;

import ch.darklions888.SpellStorm.lib.Lib;
import ch.darklions888.SpellStorm.objects.entities.projectiles.MagicalFireballEntity;
import ch.darklions888.SpellStorm.objects.entities.DummyEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.FMLPlayMessages.SpawnEntity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityInit {

	public static final DeferredRegister<EntityType<?>> REGISTER_ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Lib.MOD_ID);

	public static final RegistryObject<EntityType<MagicalFireballEntity>> MAGICAL_FIREBALL = REGISTER_ENTITIES.register(Lib.RegistryNames.MAGICAL_FIREBALL_ENTITY_STR, 
			() -> EntityType.Builder.<MagicalFireballEntity>create(MagicalFireballEntity::new, EntityClassification.MISC)
			.size(0.3125F, 0.3125F)
			.setTrackingRange(64)
			.setUpdateInterval(1)
			.setShouldReceiveVelocityUpdates(true)
			.build(Lib.RegistryNames.MAGICAL_FIREBALL_ENTITY_RS.toString()));
	public static final RegistryObject<EntityType<DummyEntity>> DummyMyDummy = 
			REGISTER_ENTITIES.register(Lib.RegistryNames.DUMMY_ENTITY_STR, 
			() -> EntityType.Builder.create(DummyEntity::new, EntityClassification.CREATURE)
            .size(1.0f, 1.0f)
            .build(Lib.RegistryNames.DUMMY_ENTITY_STR_RS.toString()));
			
}
