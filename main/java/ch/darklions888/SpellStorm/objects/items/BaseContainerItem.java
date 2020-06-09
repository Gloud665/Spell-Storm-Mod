package ch.darklions888.SpellStorm.objects.items;

import java.util.List;

import ch.darklions888.SpellStorm.enums.MagicSource;
import ch.darklions888.SpellStorm.enums.ManaContainerSize;
import ch.darklions888.SpellStorm.interfaces.IMagicalContainer;
import ch.darklions888.SpellStorm.util.helpers.ItemNBTHelper;
import ch.darklions888.SpellStorm.util.helpers.formatting.FormattingHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class BaseContainerItem extends Item implements IMagicalContainer {
	protected MagicSource[] sources;
	protected ManaContainerSize size;

	public BaseContainerItem(MagicSource[] sources, ManaContainerSize size, Properties properties) {
		super(properties);

		this.sources = sources;
		this.size = size;
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

		tooltip.add(new StringTextComponent("This magical item can store mana."));

		for (MagicSource source : sources) {
			tooltip.add(new StringTextComponent(FormattingHelper.GetSourceColor(source)
					+ FormattingHelper.GetFontFormat(source) + source.sourceId + "\u00A7r" + " Mana: "
					+ String.valueOf(getManaValue(stack, source.sourceId) + "/" + String.valueOf(getContainerSize()))));
		}

		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public int getManaValue(ItemStack stack, String key) {
		return ItemNBTHelper.getInt(stack, key, 0);
	}

	@Override
	public void setManaValue(ItemStack stack, String key, int manaAmount) {
		ItemNBTHelper.setInt(stack, key, manaAmount);
	}

	@Override
	public void addManaValue(ItemStack stack, String key, int manaAmount) {
		setManaValue(stack, key, Math.min(getManaValue(stack, key) + manaAmount, size.size));
	}

	@Override
	public int getContainerSize() {
		return size.size;
	}

	@Override
	public MagicSource[] getMagigSource() {
		return sources;
	}

	@Override
	public boolean hasMagicSource(MagicSource sourceIn) {
		for (MagicSource source : sources) {
			if (source == sourceIn) {
				return true;
			}
		}

		return false;
	}

}