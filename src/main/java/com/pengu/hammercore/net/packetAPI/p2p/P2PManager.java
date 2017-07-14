package com.pengu.hammercore.net.packetAPI.p2p;

import java.util.UUID;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.hammercore.net.packetAPI.PacketManager;

public class P2PManager
{
	public final PacketManager mgr;
	
	public P2PManager(PacketManager mgr)
	{
		this.mgr = mgr;
	}
	
	@SideOnly(Side.CLIENT)
	public void sendTo(ITask packet, String... usernames)
	{
		mgr.sendToServer(new PacketSendTaskNamed(packet, usernames));
	}
	
	@SideOnly(Side.CLIENT)
	public void sendTo(ITask packet, UUID... playerUids)
	{
		mgr.sendToServer(new PacketSendTaskUUID(packet, playerUids));
	}
	
	@SideOnly(Side.CLIENT)
	public void sendToDimension(ITask packet, int... dims)
	{
		mgr.sendToServer(new PacketSendTaskDim(packet, dims));
	}
}