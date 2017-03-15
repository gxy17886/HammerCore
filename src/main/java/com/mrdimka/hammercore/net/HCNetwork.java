package com.mrdimka.hammercore.net;

import com.mrdimka.hammercore.net.packetAPI.PacketManager;
import com.mrdimka.hammercore.net.pkt.PacketNoSpamChat;
import com.mrdimka.hammercore.net.pkt.PacketPlayBlockBreakSound;
import com.mrdimka.hammercore.net.pkt.PacketSpawnZap;

public class HCNetwork
{
	public static final PacketManager manager = new PacketManager("hammercore");
	public static void clinit() {};
}