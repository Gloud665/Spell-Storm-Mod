package ch.darklions888.SpellStorm.objects.items;

import java.util.ArrayList;
import java.util.List;

import ch.darklions888.SpellStorm.lib.MagicSource;
import ch.darklions888.SpellStorm.util.helpers.ItemNBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public interface IStoreMana {
	
	default int getManaValue(ItemStack stack, String key) {
		return ItemNBTHelper.getInt(stack, key, 0);
	}

	default void setManaValue(ItemStack stack, String key, int manaAmount) {
		ItemNBTHelper.setInt(stack, key, manaAmount);
	}

	default void addManaValue(ItemStack stack, String key, int manaAmount) {
		setManaValue(stack, key, MathHelper.clamp(getManaValue(stack, key) + manaAmount, 0, this.getMaxMana(stack)));
	}
	
	default boolean canInfuse(ItemStack infusableStack, ItemStack storeManaStack) {
		if (!(infusableStack.getItem() instanceof IStoreMana)
				|| !(storeManaStack.getItem() instanceof IStoreMana)) {
			return false;
		} else if (!(infusableStack.getItem() instanceof IStoreMana)
				|| !(storeManaStack.getItem() instanceof IHasMagic)) {
			return false;
		}

		if (storeManaStack.isEmpty() || infusableStack.isEmpty()) {
			return false;
		}

		return true;
	}
	
	default boolean canChangeToEmpty(ItemStack stackIn) {
		return false;
	}
	
	default ItemStack getChangedEmptyItem(ItemStack stackIn) {
		return stackIn;
	}
	
	/*
	 *  TODO: Rework the magicsources, store every thing in the itemstack instead in the class
	 */
	default List<MagicSource> getMagicSources(ItemStack stackIn) {
		return new ArrayList<MagicSource>();
	}
	
	default void setMagicSources(ItemStack stackIn, List<MagicSource> sourceList) {
		return;
	}
	
	int getMaxMana(ItemStack stackIn);

	public List<MagicSource> getMagicSourceList(ItemStack stackIn);
	
	public boolean hasMagicSource(ItemStack stackIn, MagicSource sourceIn);
}
