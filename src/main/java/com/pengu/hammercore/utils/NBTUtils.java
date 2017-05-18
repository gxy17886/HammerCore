package com.pengu.hammercore.utils;

import com.pengu.hammercore.utils.NumberUtils.EnumNumberType;

import net.minecraft.nbt.NBTTagCompound;

public class NBTUtils
{
	public static void writeNumberToNBT(String key, NBTTagCompound nbt, Number number)
	{
		EnumNumberType type = NumberUtils.getType(number);
		if(type != null && type != EnumNumberType.UNDEFINED)
			nbt.setByteArray(key, NumberUtils.asBytes(number));
	}
	
	public static Number readNumberFromNBT(String key, NBTTagCompound nbt)
	{
		return NumberUtils.fromBytes(nbt.getByteArray(key));
	}
}