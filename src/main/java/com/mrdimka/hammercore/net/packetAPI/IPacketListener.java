package com.mrdimka.hammercore.net.packetAPI;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface IPacketListener<REQ extends IPacket, REPLY extends IPacket>
{
	public REPLY onArrived(REQ packet, MessageContext context);
}