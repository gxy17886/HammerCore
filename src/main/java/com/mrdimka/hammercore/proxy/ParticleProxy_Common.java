package com.mrdimka.hammercore.proxy;

import java.awt.Color;

import com.mrdimka.hammercore.net.HCNetwork;
import com.mrdimka.hammercore.net.pkt.PacketSpawnZap;
import com.pengu.hammercore.client.particle.old.IOldParticle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ParticleProxy_Common
{
	public IOldParticle spawnZap(World w, Vec3d start, Vec3d end, Color rgb)
	{
		PacketSpawnZap zap = new PacketSpawnZap();
		zap.color = rgb;
		zap.start = start;
		zap.end = end;
		zap.world = w.provider.getDimension();
		HCNetwork.manager.sendToAll(zap);
		return null;
	};
	
	public IOldParticle spawnZap(int w, Vec3d start, Vec3d end, Color rgb)
	{
		PacketSpawnZap zap = new PacketSpawnZap();
		zap.color = rgb;
		zap.start = start;
		zap.end = end;
		zap.world = w;
		HCNetwork.manager.sendToAll(zap);
		return null;
	};
	
	public int getLightValue(IBlockState blockState, IBlockAccess world, BlockPos pos)
	{
		return 0;
	}
}