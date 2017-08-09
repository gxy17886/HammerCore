package com.pengu.hammercore.net.utils;

import net.minecraft.nbt.NBTTagCompound;

public class NetPropertyString extends NetPropertyAbstract<String>
{
	public NetPropertyString(IPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	public NetPropertyString(IPropertyChangeHandler handler, String initialValue)
	{
		super(handler, initialValue);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if(value != null)
			nbt.setString("val", value);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		value = nbt.getString("val");
	}
}