package ch.darklions888.SpellStorm.registries;

import ch.darklions888.SpellStorm.lib.Lib;
import ch.darklions888.SpellStorm.objects.containers.BookOfSpellsContainer;
import ch.darklions888.SpellStorm.objects.containers.MagicalForgeContainer;
import ch.darklions888.SpellStorm.objects.containers.ManaInfuserContainer;
import ch.darklions888.SpellStorm.objects.containers.SoulExtractorContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.ContainerType.IFactory;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerTypesInit {
	public static final DeferredRegister<ContainerType<?>> REGISTER_CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Lib.MOD_ID);
	
	public static final RegistryObject<ContainerType<ManaInfuserContainer>> MANA_INFUSER = REGISTER_CONTAINERS.register("mana_infuser", () -> new ContainerType<>((IFactory<ManaInfuserContainer>)ManaInfuserContainer::create));
	public static final RegistryObject<ContainerType<SoulExtractorContainer>> SOUL_EXTRACTOR = REGISTER_CONTAINERS.register("soul_extractor", () -> new ContainerType<>((IFactory<SoulExtractorContainer>)SoulExtractorContainer::create));
	public static final RegistryObject<ContainerType<BookOfSpellsContainer>> BOOK_OF_SPELLS = REGISTER_CONTAINERS.register("book_of_spells", () -> new ContainerType<>((IFactory<BookOfSpellsContainer>)BookOfSpellsContainer::create));
	public static final RegistryObject<ContainerType<MagicalForgeContainer>> MAGICAL_FORGE = REGISTER_CONTAINERS.register("magical_forge", () -> new ContainerType<>((IFactory<MagicalForgeContainer>)MagicalForgeContainer::create));
}
