package com.pengu.hammercore.asm;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import com.mrdimka.hammercore.cfg.HammerCoreConfigs;
import com.pengu.hammercore.utils.WorldLocation;

public class SnowfallHooks
{
	public static void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		int meta = state.getBlock().getMetaFromState(state);
		int k1 = meta & 0x7;
		if(world.getLightFor(EnumSkyBlock.BLOCK, pos) > 11)
		{
			if(meta < 1)
				world.setBlockToAir(pos);
			else
				world.setBlockState(pos, state.getBlock().getStateFromMeta(meta - 1));
		}
	}
	
	public static boolean canSnowAtBody(World world, BlockPos pos, boolean checkLight)
	{
		WorldLocation loc = new WorldLocation(world, pos);
		Biome biome = loc.getBiome();
		float f = biome.getFloatTemperature(pos);
		
		if(!HammerCoreConfigs.snowfall_enabled)
		{
			if(pos.getY() >= 0 && pos.getY() < 256 && world.getLightFor(EnumSkyBlock.BLOCK, pos) < 10)
			{
				IBlockState iblockstate = world.getBlockState(pos);
				if(iblockstate.getBlock().isAir(iblockstate, world, pos) && Blocks.SNOW_LAYER.canPlaceBlockAt(world, pos))
					return true;
			}
			
			return false;
		}
		
		if(!HammerCoreConfigs.snowfall_enabled)
			return false;
		
		if(loc.getBlock() == Blocks.AIR)
		{
			if(f >= 0.15F && !HammerCoreConfigs.snowfall_snowWorld)
				return false;
			else if(!checkLight)
				return true;
			else
			{
				if(pos.getY() >= 0 && pos.getY() < 256 && world.getLightFor(EnumSkyBlock.BLOCK, pos) < 10)
				{
					IBlockState iblockstate = world.getBlockState(pos);
					if(iblockstate.getBlock().isAir(iblockstate, world, pos) && Blocks.SNOW_LAYER.canPlaceBlockAt(world, pos))
						return true;
				}
				return false;
			}
		}
		
		if(checkLight && world.getLightFor(EnumSkyBlock.BLOCK, pos) < 10 && loc.getBlock() == Blocks.SNOW_LAYER && !world.isRemote)
		{
			int smallest = loc.getMeta();
			
			if(world.rand.nextInt(smallest * 2 + 2) == 0)
			{
				loc = loc.offset(-1, 0, -1);
				
				WorldLocation l = loc;
				int least = loc.getMeta();
				
				for(int i = 0; i < 3; ++i)
					for(int j = 0; j < 3; ++j)
					{
						WorldLocation tl = loc.offset(i - 1, 0, j - 1);
						if(tl.getBlock() == Blocks.SNOW_LAYER && tl.getMeta() < least)
						{
							least = tl.getMeta();
							l = tl;
						}
					}
				
				int m = l.getMeta();
				if(m < 7)
					l.setMeta(l.getMeta() + 1);
				
				return true;
			}
			
			return false;
		}
		
		return false;
	}
	
	private static int findSmallest(WorldLocation loc, int smallest)
	{
		if(loc.getBlock() == Blocks.SNOW_LAYER)
			return Math.min(loc.getMeta(), smallest);
		if(Blocks.SNOW_LAYER.canPlaceBlockAt(loc.getWorld(), loc.getPos()))
			return -1;
		return smallest;
	}
	
	private static void grow(WorldLocation loc, int layers)
	{
		if((loc.getBlock() == Blocks.SNOW_LAYER && loc.getMeta() == layers) || (layers == -1 && Blocks.SNOW_LAYER.canPlaceBlockAt(loc.getWorld(), loc.getPos())))
			loc.setMeta(layers + 1);
	}
}