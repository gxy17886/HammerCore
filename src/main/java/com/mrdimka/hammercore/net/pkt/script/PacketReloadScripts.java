package com.mrdimka.hammercore.net.pkt.script;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.mrdimka.hammercore.HammerCore.GRCProvider;
import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.net.packetAPI.IPacketListener;

public class PacketReloadScripts implements IPacket, IPacketListener<PacketReloadScripts, IPacket>
{

	@Override
	public IPacket onArrived(PacketReloadScripts packet, MessageContext context)
	{
		GRCProvider.reloadScript();
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {}
}