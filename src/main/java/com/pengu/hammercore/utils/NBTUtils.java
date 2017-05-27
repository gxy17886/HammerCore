package com.pengu.hammercore.utils;

import net.minecraft.nbt.NBTTagCompound;

import com.pengu.hammercore.utils.NumberUtils.EnumNumberType;

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