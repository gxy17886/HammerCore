package com.pengu.hammercore.net.utils;

import java.nio.ByteBuffer;

import com.pengu.hammercore.utils.NBTUtils;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public class NetPropertyBool extends NetPropertyAbstract<Boolean>
{
	public NetPropertyBool(IPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	public NetPropertyBool(IPropertyChangeHandler handler, boolean initialValue)
	{
		super(handler, initialValue);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setBoolean("val", value);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		value = nbt.getBoolean("val");
	}
}