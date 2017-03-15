package com.mrdimka.hammercore.proxy;

import java.awt.Color;

import com.mrdimka.hammercore.client.particles.IParticle;
import com.mrdimka.hammercore.net.HCNetwork;
import com.mrdimka.hammercore.net.pkt.PacketSpawnZap;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ParticleProxy_Common
{
	public IParticle spawnZap(World w, Vec3d start, Vec3d end, Color rgb)
	{
		PacketSpawnZap zap = new PacketSpawnZap();
		zap.color = rgb;
		zap.start = start;
		zap.end = end;
		zap.world = w.provider.getDimension();
		HCNetwork.manager.sendToAll(zap);
		return null;
	};
	
	public IParticle spawnZap(int w, Vec3d start, Vec3d end, Color rgb)
	{
		PacketSpawnZap zap = new PacketSpawnZap();
		zap.color = rgb;
		zap.start = start;
		zap.end = end;
		zap.world = w;
		HCNetwork.manager.sendToAll(zap);
		return null;
	};
}