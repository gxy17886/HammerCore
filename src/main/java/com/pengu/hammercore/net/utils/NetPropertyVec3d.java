package com.pengu.hammercore.net.utils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

public class NetPropertyVec3d extends NetPropertyAbstract<Vec3d>
{
	public NetPropertyVec3d(IPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	public NetPropertyVec3d(IPropertyChangeHandler handler, Vec3d initialValue)
	{
		super(handler, initialValue);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if(value != null)
		{
			nbt.setDouble("x", value.xCoord);
			nbt.setDouble("y", value.yCoord);
			nbt.setDouble("z", value.zCoord);
		}
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("x"))
			value = new Vec3d(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"));
	}
}