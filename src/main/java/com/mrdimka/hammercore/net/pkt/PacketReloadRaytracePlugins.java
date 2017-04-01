package com.mrdimka.hammercore.net.pkt;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.net.packetAPI.IPacketListener;

public class PacketReloadRaytracePlugins implements IPacket, IPacketListener<PacketReloadRaytracePlugins, IPacket>
{

	@Override
	public IPacket onArrived(PacketReloadRaytracePlugins packet, MessageContext context)
	{
		HammerCore.instance.reloadRaytracePlugins();
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {}
}