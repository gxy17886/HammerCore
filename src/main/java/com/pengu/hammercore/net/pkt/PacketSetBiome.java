package com.pengu.hammercore.net.pkt;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.pengu.hammercore.net.packetAPI.IPacket;
import com.pengu.hammercore.net.packetAPI.IPacketListener;

public class PacketSetBiome implements IPacket, IPacketListener<PacketSetBiome, IPacket>
{
	int x, z, id;
	byte biome;
	
	public PacketSetBiome(int x, int z, int id, byte biome)
	{
		this.x = x;
		this.z = z;
		this.id = id;
		this.biome = biome;
	}
	
	public PacketSetBiome()
	{
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setIntArray("d", new int[] { x, z, id, biome });
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		int[] d = nbt.getIntArray("d");
		x = d[0];
		z = d[1];
		id = d[2];
		biome = (byte) d[3];
	}
	
	@Override
	public IPacket onArrived(PacketSetBiome packet, MessageContext context)
	{
		return null;
	}
}