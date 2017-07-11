package com.pengu.hammercore.proxy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.pengu.hammercore.client.particle.old.IOldParticle;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.net.pkt.PacketSpawnSlowZap;
import com.pengu.hammercore.net.pkt.PacketSpawnZap;

public class ParticleProxy_Common
{
	public IOldParticle spawnZap(World w, Vec3d start, Vec3d end, int rgb)
	{
		PacketSpawnZap zap = new PacketSpawnZap();
		zap.color = rgb;
		zap.start = start;
		zap.end = end;
		zap.world = w.provider.getDimension();
		HCNetwork.manager.sendToAll(zap);
		return null;
	}
	
	public IOldParticle spawnZap(int w, Vec3d start, Vec3d end, int rgb)
	{
		PacketSpawnZap zap = new PacketSpawnZap();
		zap.color = rgb;
		zap.start = start;
		zap.end = end;
		zap.world = w;
		HCNetwork.manager.sendToAll(zap);
		return null;
	}
	
	public IOldParticle spawnSlowZap(World w, Vec3d start, Vec3d end, int rgb, int maxTicks, float ampl)
	{
		PacketSpawnSlowZap zap = new PacketSpawnSlowZap();
		zap.color = rgb;
		zap.start = start;
		zap.end = end;
		zap.world = w.provider.getDimension();
		zap.maxTicks = maxTicks;
		zap.ampl = ampl;
		HCNetwork.manager.sendToAll(zap);
		return null;
	}
	
	public IOldParticle spawnSlowZap(int w, Vec3d start, Vec3d end, int rgb, int maxTicks, float ampl)
	{
		PacketSpawnSlowZap zap = new PacketSpawnSlowZap();
		zap.color = rgb;
		zap.start = start;
		zap.end = end;
		zap.world = w;
		zap.maxTicks = maxTicks;
		zap.ampl = ampl;
		HCNetwork.manager.sendToAll(zap);
		return null;
	}
	
	public int getLightValue(IBlockState blockState, IBlockAccess world, BlockPos pos)
	{
		return 0;
	}
}