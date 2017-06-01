package com.mrdimka.hammercore.net;

import com.mrdimka.hammercore.net.packetAPI.PacketManager;

public class HCNetwork
{
	public static final PacketManager manager = new PacketManager("hammercore");
	
	public static PacketManager getManager(String name)
	{
		return manager;
	}
	
	public static void clinit()
	{
	};
}