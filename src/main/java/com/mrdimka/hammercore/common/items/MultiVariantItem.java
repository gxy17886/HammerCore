package com.mrdimka.hammercore.common.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class MultiVariantItem extends Item
{
	public final String[] names;
	
	public MultiVariantItem(String registryName, String... subitems)
	{
		names = subitems;
		setUnlocalizedName(registryName);
		setHasSubtypes(true);
	}
	
	protected final void insertPrefix(String prefix)
	{
		for(int i = 0; i < names.length; ++i)
			names[i] = prefix + names[i];
	}
	
	protected final void insertPostfix(String postfix)
	{
		for(int i = 0; i < names.length; ++i)
			names[i] = names[i] + postfix;
	}
	
	public String getUnlocalizedName(ItemStack stack)
	{
		return "item." + (stack.getItemDamage() < names.length ? names[stack.getItemDamage()] : "unnamed");
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> l)
	{
		for(int i = 0; i < names.length; ++i)
			l.add(new ItemStack(item, 1, i));
	}
}