package com.pengu.hammercore.net.utils;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;

public class NetPropertyUUID extends NetPropertyAbstract<UUID>
{
	public NetPropertyUUID(IPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	public NetPropertyUUID(IPropertyChangeHandler handler, UUID initialValue)
	{
		super(handler, initialValue);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if(value != null)
			nbt.setUniqueId("val", value);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		value = nbt.getUniqueId("val");
	}
}