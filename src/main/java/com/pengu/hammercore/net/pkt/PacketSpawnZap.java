package com.pengu.hammercore.net.pkt;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.net.packetAPI.IPacket;
import com.pengu.hammercore.net.packetAPI.IPacketListener;

public class PacketSpawnZap implements IPacket, IPacketListener<PacketSpawnZap, IPacket>
{
	public int world;
	public Vec3d start, end;
	public int color;
	
	@Override
	public IPacket onArrived(PacketSpawnZap packet, MessageContext context)
	{
		HammerCore.particleProxy.spawnZap(packet.world, packet.start, packet.end, packet.color);
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setDouble("sx", start.x);
		nbt.setDouble("sy", start.y);
		nbt.setDouble("sz", start.z);
		
		nbt.setDouble("ex", end.x);
		nbt.setDouble("ey", end.y);
		nbt.setDouble("ez", end.z);
		
		nbt.setInteger("dm", world);
		
		nbt.setInteger("cl", color);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		start = new Vec3d(nbt.getDouble("sx"), nbt.getDouble("sy"), nbt.getDouble("sz"));
		end = new Vec3d(nbt.getDouble("ex"), nbt.getDouble("ey"), nbt.getDouble("ez"));
		world = nbt.getInteger("dm");
		color = nbt.getInteger("cl");
	}
}