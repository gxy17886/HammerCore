package com.pengu.hammercore.net.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class NetPropertyItemStack extends NetPropertyAbstract<ItemStack>
{
	public NetPropertyItemStack(IPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	public NetPropertyItemStack(IPropertyChangeHandler handler, ItemStack initialValue)
	{
		super(handler, initialValue);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		get().writeToNBT(nbt);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		value = ItemStack.loadItemStackFromNBT(nbt);
	}
}