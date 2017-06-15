package com.pengu.hammercore.net.packetAPI;

import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface IPacketListener<REQ extends IPacket, REPLY extends IPacket>
{
	public REPLY onArrived(REQ packet, MessageContext context);
}