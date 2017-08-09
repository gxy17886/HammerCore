package com.mrdimka.hammercore.net.packetAPI;

import net.minecraft.nbt.NBTTagCompound;

public interface IPacket
{
	public void writeToNBT(NBTTagCompound nbt);
	
	public void readFromNBT(NBTTagCompound nbt);
}