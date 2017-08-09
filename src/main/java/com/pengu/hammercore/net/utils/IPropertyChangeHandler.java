package com.pengu.hammercore.net.utils;

import net.minecraft.nbt.NBTTagCompound;

public interface IPropertyChangeHandler
{
	int registerProperty(NetPropertyAbstract prop);
	
	void load(int id, NBTTagCompound nbt);
	
	void notifyOfChange(NetPropertyAbstract prop);
	
	void sendChangesToNearby();
}