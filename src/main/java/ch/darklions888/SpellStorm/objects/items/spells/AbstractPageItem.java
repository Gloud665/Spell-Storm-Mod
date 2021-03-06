package ch.darklions888.SpellStorm.objects.items.spells;

import java.util.ArrayList;
import java.util.List;

import ch.darklions888.SpellStorm.lib.Lib;
import ch.darklions888.SpellStorm.lib.MagicSource;
import ch.darklions888.SpellStorm.objects.items.IMagicalPageItem;
import ch.darklions888.SpellStorm.objects.items.IStoreMana;
import ch.darklions888.SpellStorm.util.helpers.formatting.FormattingHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public abstract class AbstractPageItem extends Item implements IMagicalPageItem, IStoreMana
{
	protected MagicSource defaultManaSource;
	protected final List<MagicSource> magicSourceList;
	protected final int containingManaSize;
	protected final int maxMana;
	protected int manaConsumption;
	protected final TextFormatting format;
	protected final boolean hasEffect;
	protected int coolDownTick = 0;
	protected int maxRange = 0;
	
	public AbstractPageItem(int size, MagicSource source, int manaConsumption, TextFormatting format, boolean hasEffect, Properties properties)
	{
		super(properties);
		this.containingManaSize = size;
		this.maxMana = size;
		this.defaultManaSource = source;
		this.manaConsumption = manaConsumption;
		this.magicSourceList = new ArrayList<>();
		this.magicSourceList.add(source);
		this.format = format;
		this.hasEffect = hasEffect;
	}
	
	protected boolean canCast(ItemStack stackIn, PlayerEntity playerIn) {
		return this.getManaValue(stackIn, this.defaultManaSource.getId()) >= this.manaConsumption || playerIn.isCreative();
	}
	
	protected void consumMana(ItemStack stackIn, MagicSource sourceIn) {
		this.addManaValue(stackIn, sourceIn.getId(), -manaConsumption);
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return hasEffect;
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack) {
		return stack.getItem() instanceof IMagicalPageItem ? 1 : super.getItemStackLimit(stack);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		return this.getAbilities(worldIn, playerIn, handIn, playerIn.getHeldItem(handIn), null);
	}
	
	@Override
	public abstract ActionResult<ItemStack> getAbilities(World worldIn, PlayerEntity playerIn, Hand handIn, ItemStack stackIn, ItemStack bookIn);
	
	@Override 
	public abstract int getInkColor();
	
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) 
	{
		String sourceName = this.defaultManaSource.getSourceName().getString();
		IFormattableTextComponent manaCText = new StringTextComponent(String.valueOf(this.manaConsumption)).mergeStyle(TextFormatting.LIGHT_PURPLE);
		IFormattableTextComponent manaValue = new StringTextComponent(String.valueOf(this.getManaValue(stack, this.defaultManaSource.getId()))).mergeStyle(TextFormatting.LIGHT_PURPLE);
		IFormattableTextComponent containerSize = new StringTextComponent(String.valueOf(this.containingManaSize)).mergeStyle(TextFormatting.LIGHT_PURPLE);
		
		tooltip.add(new StringTextComponent("").append(manaValue).append(new StringTextComponent("/")).append(containerSize).append(new StringTextComponent(Lib.TextComponents.MANA_LEFT.getString()).mergeStyle(TextFormatting.RESET)));
		tooltip.add(new StringTextComponent(Lib.TextComponents.DESC_MANA_CONSUMPTION.getString()).append(manaCText));
		tooltip.add(new StringTextComponent(FormattingHelper.GetSourceColor(this.defaultManaSource) + FormattingHelper.GetFontFormat(this.defaultManaSource) + sourceName + "\u00A7r" + " ").append(Lib.TextComponents.MANA_SOURCE));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
	
	@Override
	public ITextComponent getDisplayName(ItemStack stack) 
	{
		if(format == null)
		{
			return new TranslationTextComponent(this.getTranslationKey(stack));
		}
		else
		{
			TranslationTextComponent translationText = new TranslationTextComponent(this.getTranslationKey(stack));
			return new TranslationTextComponent(format + translationText.getString() + "  [" + String.valueOf(this.getManaValue(stack, this.defaultManaSource.getId())) + "/" + this.containingManaSize + "]");
		}
	}
	
	protected void setCooldown(PlayerEntity playerIn, int coolDownTicks, ItemStack ... itemIn) {
		for (ItemStack i : itemIn) {
			if (i != null)
				playerIn.getCooldownTracker().setCooldown(i.getItem(), coolDownTicks);
		}
	}
	
	protected void setCooldown(PlayerEntity playerIn, ItemStack ... itemIn) {
		this.setCooldown(playerIn, this.coolDownTick, itemIn);
	}
	
	@Override
	public int getMaxMana(ItemStack stackIn) {
		return this.maxMana;
	}

	@Override
	public List<MagicSource> getMagicSourceList(ItemStack stackIn) {
		return this.magicSourceList;
	}

	@Override
	public boolean hasMagicSource(ItemStack stackIn, MagicSource sourceIn) {
		return this.magicSourceList.contains(sourceIn);
	}
}
