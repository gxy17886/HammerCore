package com.pengu.hammercore.net.utils;

import net.minecraft.nbt.NBTTagCompound;

import com.pengu.hammercore.utils.NBTUtils;

public class NetPropertyNumber<T extends Number> extends NetPropertyAbstract<T>
{
	public NetPropertyNumber(IPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	public NetPropertyNumber(IPropertyChangeHandler handler, T initialValue)
	{
		super(handler, initialValue);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		NBTUtils.writeNumberToNBT("Val", nbt, value);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		if(!nbt.hasKey("Val") && nbt.hasKey("val"))
			value = (T) NBTUtils.readNumberFromNBT("val", nbt);
		else
			value = (T) NBTUtils.readNumberFromNBT("Val", nbt);
	}
	
	@Override
	public T get()
	{
		T t = super.get();
		return t != null ? t : (T) (Number) 0;
	}
}