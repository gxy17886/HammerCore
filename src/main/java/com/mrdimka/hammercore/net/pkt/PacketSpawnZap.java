package com.mrdimka.hammercore.net.pkt;

import java.awt.Color;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.net.packetAPI.IPacketListener;

public class PacketSpawnZap implements IPacket, IPacketListener<PacketSpawnZap, IPacket>
{
	public int world;
	public Vec3d start, end;
	public Color color;
	
	@Override
	public IPacket onArrived(PacketSpawnZap packet, MessageContext context)
	{
		HammerCore.particleProxy.spawnZap(packet.world, packet.start, packet.end, packet.color);
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setDouble("sx", start.xCoord);
		nbt.setDouble("sy", start.yCoord);
		nbt.setDouble("sz", start.zCoord);
		
		nbt.setDouble("ex", end.xCoord);
		nbt.setDouble("ey", end.yCoord);
		nbt.setDouble("ez", end.zCoord);
		
		nbt.setInteger("dm", world);
		
		nbt.setInteger("cl", color.getRGB());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		start = new Vec3d(nbt.getDouble("sx"), nbt.getDouble("sy"), nbt.getDouble("sz"));
		end = new Vec3d(nbt.getDouble("ex"), nbt.getDouble("ey"), nbt.getDouble("ez"));
		world = nbt.getInteger("dm");
		color = new Color(nbt.getInteger("cl"));
	}
}