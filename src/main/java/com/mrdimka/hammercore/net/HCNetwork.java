package com.mrdimka.hammercore.net;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import com.mrdimka.hammercore.net.packetAPI.PacketManager;
import com.mrdimka.hammercore.net.pkt.PacketParticle;

public class HCNetwork
{
	public static final PacketManager manager = new PacketManager("hammercore");
	
	public static PacketManager getManager(String name)
	{
		return manager;
	}
	
	public static void spawnParticle(World world, EnumParticleTypes particle, double x, double y, double z, double motionX, double motionY, double motionZ, int... args)
	{
		manager.sendToAllAround(new PacketParticle(world, particle, new Vec3d(x, y, z), new Vec3d(motionX, motionY, motionZ), args), new TargetPoint(world.provider.getDimension(), x, y, z, 64));
	}
	
	public static void clinit()
	{
	};
}