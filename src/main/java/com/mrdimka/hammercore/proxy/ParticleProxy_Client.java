package com.mrdimka.hammercore.proxy;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.mrdimka.hammercore.client.particles.IParticle;
import com.mrdimka.hammercore.client.particles.ParticleZap;

public class ParticleProxy_Client extends ParticleProxy_Common
{
	@Override
	public IParticle spawnZap(World w, Vec3d start, Vec3d end, Color rgb)
	{
		if(!w.isRemote)
			return super.spawnZap(w, start, end, rgb);
		ParticleZap zap = new ParticleZap(w, start.xCoord, start.yCoord, start.zCoord, end.xCoord, end.yCoord, end.zCoord, rgb.getRed() / (float) 0xFF, rgb.getGreen() / (float) 0xFF, rgb.getBlue() / (float) 0xFF);
		zap.spawn();
		return zap;
	}
	
	@Override
	public IParticle spawnZap(int w, Vec3d start, Vec3d end, Color rgb)
	{
		ParticleZap zap = null;
		if(Minecraft.getMinecraft().theWorld.provider.getDimension() == w)
		{
			zap = new ParticleZap(Minecraft.getMinecraft().theWorld, start.xCoord, start.yCoord, start.zCoord, end.xCoord, end.yCoord, end.zCoord, rgb.getRed() / (float) 0xFF, rgb.getGreen() / (float) 0xFF, rgb.getBlue() / (float) 0xFF);
			zap.spawn();
		}
		return zap;
	}
}