package com.mrdimka.hammercore.net;

import java.util.HashMap;
import java.util.Map;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.net.packetAPI.PacketManager;

public class HCNetwork
{
	public static final PacketManager manager = new PacketManager("hammercore");
	
	private static final Map<String, PacketManager> managers = new HashMap<>();
	
	static
	{
		for(String key : HammerCore.initHCChannels)
			managers.put(key, new PacketManager("custom" + key));
	}
	
	public static PacketManager getManager(String name)
	{
		if(name == null || name.equals("hammercore"))
			return manager;
		return managers.get(name);
	}
	
	public static void clinit()
	{
	};
}