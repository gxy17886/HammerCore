package com.pengu.hammercore.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

import com.pengu.hammercore.annotations.MCFBus;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.utils.IndexedMap;

@MCFBus
public class WorldGenHelper
{
	public static final Map<String, List<BlockData>> datas = new IndexedMap<>();
	
	/** Some handy {@link IBlockState} checkers for {@link #generateFlower} */
	public static final Predicate<IBlockState> //
	        GRASS_OR_DIRT_CHECKER = state -> state != null && (state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.FARMLAND), //
	        NETHERRACK_CHECKER = state -> state != null && state.getBlock() == Blocks.NETHERRACK, //
	        END_STONE_CHECKER = state -> state != null && state.getBlock() == Blocks.END_STONE;
	
	/**
	 * Generates a flower. WARNING: This method obtains world's height so you
	 * don't have to specify y level
	 */
	public static void generateFlowerOnSameY(IBlockState flower, Random rand, World world, BlockPos basePos, int maxSpawnRad, int minFlowers, int maxFlowers, boolean oneChunkOnly, Predicate<IBlockState> soilChecker)
	{
		int count = minFlowers + rand.nextInt(maxFlowers - minFlowers);
		int fails = 0;
		
		while(count > 0)
		{
			boolean planted = false;
			
			int x = basePos.getX() + rand.nextInt(maxSpawnRad) - rand.nextInt(maxSpawnRad);
			int z = basePos.getZ() + rand.nextInt(maxSpawnRad) - rand.nextInt(maxSpawnRad);
			
			BlockPos pos = new BlockPos(x, basePos.getY() - 1, z);
			
			if(oneChunkOnly && (pos.getX() >> 4 != basePos.getX() >> 4 || pos.getZ() >> 4 != basePos.getZ() >> 4))
				planted = false;
			else if(soilChecker.test(world.getBlockState(pos)) && world.getBlockState(pos.up()).getBlock().isReplaceable(world, pos.up()))
			{
				setBlockState(world, pos.up(), flower, null);
				planted = true;
			}
			
			if(!planted)
				++fails;
			
			if(fails >= 10 || planted)
			{
				fails = 0;
				--count;
			}
		}
	}
	
	public static void generateFlower(IBlockState flower, Random rand, World world, BlockPos basePos, int maxSpawnRad, int minFlowers, int maxFlowers, boolean oneChunkOnly, Predicate<IBlockState> soilChecker)
	{
		int count = minFlowers + rand.nextInt(maxFlowers - minFlowers);
		int fails = 0;
		
		while(count > 0)
		{
			boolean planted = false;
			
			int x = basePos.getX() + rand.nextInt(maxSpawnRad) - rand.nextInt(maxSpawnRad);
			int z = basePos.getZ() + rand.nextInt(maxSpawnRad) - rand.nextInt(maxSpawnRad);
			
			BlockPos pos = new BlockPos(x, world.getHeight(x, z) - 1, z);
			
			if(oneChunkOnly && (pos.getX() >> 4 != basePos.getX() >> 4 || pos.getZ() >> 4 != basePos.getZ() >> 4))
				planted = false;
			else if(soilChecker.test(world.getBlockState(pos)) && world.getBlockState(pos.up()).getBlock().isReplaceable(world, pos.up()))
			{
				setBlockState(world, pos.up(), flower, null);
				planted = true;
			}
			
			if(!planted)
				++fails;
			
			if(fails >= 10 || planted)
			{
				fails = 0;
				--count;
			}
		}
	}
	
	public static File getBlockSaveFile()
	{
		return WorldUtil.getWorldSubfile("pengu-pending_blocks.cbd");
	}
	
	/**
	 * An attempt to prevent FML's cascading chunkgen lag.
	 */
	public static void setBlockState(World world, BlockPos pos, IBlockState state, @Nullable TileEntity tile)
	{
		String key = world.provider.getDimension() + "";
		List<BlockData> ds = datas.get(key);
		if(ds == null)
			datas.put(key, ds = new ArrayList<>());
		ds.add(new BlockData(world, pos, state, tile));
	}
	
	/**
	 * An attempt to prevent FML's cascading chunkgen lag.
	 */
	public static void setBlockState(World world, BlockPos pos, IBlockState state)
	{
		setBlockState(world, pos, state, null);
	}
	
	/**
	 * An attempt to prevent FML's cascading chunkgen lag.
	 */
	public static void setBlockState(World world, BlockPos pos, IBlockState state, int marker, @Nullable TileEntity tile)
	{
		String key = world.provider.getDimension() + "";
		List<BlockData> ds = datas.get(key);
		if(ds == null)
			datas.put(key, ds = new ArrayList<>());
		ds.add(new BlockData(world, pos, state, marker, tile));
	}
	
	/**
	 * An attempt to prevent FML's cascading chunkgen lag.
	 */
	public static void setBlockState(World world, BlockPos pos, IBlockState state, int marker)
	{
		setBlockState(world, pos, state, marker, null);
	}
	
	@SubscribeEvent
	public void worldSaveEvt(WorldEvent.Save evt)
	{
		try
		{
			ObjectOutputStream o = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(getBlockSaveFile())));
			o.writeObject(datas);
			o.close();
		} catch(Throwable err)
		{
		}
	}
	
	@SubscribeEvent
	public void worldLoadEvt(WorldEvent.Load evt)
	{
		try
		{
			ObjectInputStream i = new ObjectInputStream(new GZIPInputStream(new FileInputStream(getBlockSaveFile())));
			datas.putAll((Map) i.readObject());
			i.close();
		} catch(Throwable err)
		{
		}
	}
	
	@SubscribeEvent
	public void worldTick(WorldTickEvent evt)
	{
		if(evt.phase != Phase.START)
			return;
		evt.world.profiler.startSection("Hammer Core BlockGen");
		String key = evt.world.provider.getDimension() + "";
		List<BlockData> ds = datas.get(key);
		if(ds != null)
		{
			for(int i = 0; i < ds.size(); ++i)
			{
				ds.get(i).place(evt.world);
				ds.remove(i);
			}
		}
		evt.world.profiler.endSection();
	}
}