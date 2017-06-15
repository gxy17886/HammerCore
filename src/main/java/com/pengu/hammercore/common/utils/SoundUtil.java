package com.pengu.hammercore.common.utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.hammercore.HammerCore;

/**
 * In the MC-forge v1.11 this class will be removed
 * 
 * @deprecated Use {@link HammerCore#audioProxy} instead.
 **/
@Deprecated
public class SoundUtil
{
	@Deprecated
	public static void playSoundEffect(World world, String sound, BlockPos pos, float volume, float pitch, SoundCategory category)
	{
		HammerCore.audioProxy.playSoundAt(world, sound, pos, volume, pitch, category);
	}
	
	@Deprecated
	public static void playSoundEffect(World world, String sound, double x, double y, double z, float volume, float pitch, SoundCategory category)
	{
		HammerCore.audioProxy.playSoundAt(world, sound, x, y, z, volume, pitch, category);
	}
	
	@Deprecated
	public static void playBlockStateBreak(World world, IBlockState type, double x, double y, double z, float volume, float pitch, SoundCategory category)
	{
		HammerCore.audioProxy.playBlockStateBreak(world, type, x, y, z, volume, pitch, category);
	}
}