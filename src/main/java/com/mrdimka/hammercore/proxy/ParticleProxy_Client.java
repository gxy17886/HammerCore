package com.mrdimka.hammercore.proxy;

import java.awt.Color;
import java.lang.reflect.Field;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import com.mrdimka.hammercore.api.dynlight.DynamicLightGetter;
import com.mrdimka.hammercore.client.particles.IParticle;
import com.mrdimka.hammercore.client.particles.ParticleZap;

public class ParticleProxy_Client extends ParticleProxy_Common
{
	{
		MinecraftForge.EVENT_BUS.register(new DynamicLightGetter());
	}
	
	@Override
	public IParticle spawnZap(World w, Vec3d start, Vec3d end, Color rgb)
	{
		if(!w.isRemote) return super.spawnZap(w, start, end, rgb);
		ParticleZap zap = new ParticleZap(w, start.xCoord, start.yCoord, start.zCoord, end.xCoord, end.yCoord, end.zCoord, rgb.getRed() / (float) 0xFF, rgb.getGreen() / (float) 0xFF, rgb.getBlue() / (float) 0xFF);
		zap.spawn();
		return zap;
	}
	
	@Override
	public IParticle spawnZap(int w, Vec3d start, Vec3d end, Color rgb)
	{
		ParticleZap zap = null;
		if(Minecraft.getMinecraft().world.provider.getDimension() == w)
		{
			zap = new ParticleZap(Minecraft.getMinecraft().world, start.xCoord, start.yCoord, start.zCoord, end.xCoord, end.yCoord, end.zCoord, rgb.getRed() / (float) 0xFF, rgb.getGreen() / (float) 0xFF, rgb.getBlue() / (float) 0xFF);
			zap.spawn();
		}
		return zap;
	}
	
	@Override
	public int getLightValue(IBlockState blockState, IBlockAccess world, BlockPos pos)
	{
		return DynamicLightGetter.getLightValue(blockState, world, pos);
	}
	
	private static final Field motionX, motionY, motionZ, posX, posY, posZ;
	
	static
	{
		Field[] pf = Particle.class.getDeclaredFields();
		
		posX = pf[5];
		posX.setAccessible(true);
		
		posY = pf[6];
		posY.setAccessible(true);
		
		posZ = pf[7];
		posZ.setAccessible(true);
		
		motionX = pf[8];
		motionX.setAccessible(true);
		
		motionY = pf[9];
		motionY.setAccessible(true);
		
		motionZ = pf[10];
		motionZ.setAccessible(true);
	}
	
	public static double getParticleMotionX(Particle part)
	{
		try { return motionX.getDouble(part); }catch(Throwable err) {}
		return 0;
	}
	
	public static double getParticleMotionY(Particle part)
	{
		try { return motionY.getDouble(part); }catch(Throwable err) {}
		return 0;
	}
	
	public static double getParticleMotionZ(Particle part)
	{
		try { return motionZ.getDouble(part); }catch(Throwable err) {}
		return 0;
	}
	
	public static double getParticlePosX(Particle part)
	{
		try { return posX.getDouble(part); }catch(Throwable err) {}
		return 0;
	}
	
	public static double getParticlePosY(Particle part)
	{
		try { return posY.getDouble(part); }catch(Throwable err) {}
		return 0;
	}
	
	public static double getParticlePosZ(Particle part)
	{
		try { return posZ.getDouble(part); }catch(Throwable err) {}
		return 0;
	}
	
	public static void setParticleMotionX(Particle part, double d) { try { motionX.setDouble(part, d); }catch(Throwable err) {}}
	public static void setParticleMotionY(Particle part, double d) { try { motionY.setDouble(part, d); }catch(Throwable err) {}}
	public static void setParticleMotionZ(Particle part, double d) { try { motionZ.setDouble(part, d); }catch(Throwable err) {}}
	public static void setParticlePosX(Particle part, double d) { try { posX.setDouble(part, d); }catch(Throwable err) {}}
	public static void setParticlePosY(Particle part, double d) { try { posY.setDouble(part, d); }catch(Throwable err) {}}
	public static void setParticlePosZ(Particle part, double d) { try { posZ.setDouble(part, d); }catch(Throwable err) {}}
}