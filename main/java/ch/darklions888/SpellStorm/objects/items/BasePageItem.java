package ch.darklions888.SpellStorm.objects.items;

import java.util.List;

import ch.darklions888.SpellStorm.enums.MagicSource;
import ch.darklions888.SpellStorm.enums.ManaContainerSize;
import ch.darklions888.SpellStorm.enums.ManaPower;
import ch.darklions888.SpellStorm.interfaces.IMagicalPageItem;
import ch.darklions888.SpellStorm.util.helpers.ItemNBTHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class BasePageItem extends BaseItem implements IMagicalPageItem
{
	protected static final String MANA_TAG = "mana";
	private MagicSource source;
	protected int size = 0;
	
	public BasePageItem(ManaContainerSize size, MagicSource source, ManaPower mana, TextFormatting format, boolean hasEffect, Properties properties)
	{
		super(source, mana, format, hasEffect, properties);
		this.size = size.size;
		this.source = source;
	}

	@Override
	public int getMana(ItemStack stackIn) 
	{
		return ItemNBTHelper.getInt(stackIn, MANA_TAG, 0);
	}

	@Override
	public int getMaxContainerSize(ItemStack stackIn) 
	{
		return this.size;
	}

	@Override
	public void addMana(ItemStack stackIn, int manaAmount) 
	{
		setMana(stackIn, Math.min(getMana(stackIn) + manaAmount, size));
	}

	@Override
	public boolean canReceiveManaFromtItem(ItemStack stack1, ItemStack stack2) 
	{
		if(stack1.getItem() instanceof BasePageItem && stack2.getItem() instanceof BaseItem)
		{
			BasePageItem page = (BasePageItem) stack1.getItem();
			BaseItem base = (BaseItem) stack2.getItem();
			
			return page.magicSource() == base.magicSource();
		}
		else
		{
			return false;
		}
	}

	@Override
	public void setMana(ItemStack stackIn, int manaAmount) 
	{
		ItemNBTHelper.setInt(stackIn, MANA_TAG, manaAmount);
	}

	@Override
	public MagicSource magicSource() 
	{
		return this.source;
	}
}