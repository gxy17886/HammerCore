package com.pengu.hammercore.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.pengu.hammercore.annotations.MCFBus;
import com.pengu.hammercore.common.chunk.ChunkPredicate.IChunkLoader;
import com.pengu.hammercore.common.chunk.ChunkPredicate.LoadableChunk;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.event.WorldEventsHC;
import com.pengu.hammercore.utils.IndexedMap;

@MCFBus
public class WorldGenHelper
{
	public static final Map<Integer, List<Long>> CHUNKLOADERS = new IndexedMap<>();
	private static final ArrayList<LoadableChunk> LOADED_CHUNKS = new ArrayList<>();
	
	/** Some handy {@link IBlockState} checkers for {@link #generateFlower} */
	public static final Predicate<IBlockState> //
	        GRASS_OR_DIRT_CHECKER = state -> state != null && (state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.DIRT || state.getBlock() == Blocks.FARMLAND), //
	        NETHERRACK_CHECKER = state -> state != null && state.getBlock() == Blocks.NETHERRACK, //
	        END_STONE_CHECKER = state -> state != null && state.getBlock() == Blocks.END_STONE;
	
	public static void loadChunk(int world, BlockPos chunkloader)
	{
		List<Long> longs = WorldGenHelper.CHUNKLOADERS.get(world);
		if(longs == null)
			WorldGenHelper.CHUNKLOADERS.put(world, longs = new ArrayList<>());
		if(!longs.contains(chunkloader.toLong()))
			longs.add(chunkloader.toLong());
		reloadChunks();
	}
	
	public static IChunkLoader chunkLoader()
	{
		return () -> LOADED_CHUNKS;
	}
	
	public static void unloadChunk(int world, BlockPos chunkloader)
	{
		List<Long> longs = WorldGenHelper.CHUNKLOADERS.get(world);
		if(longs == null)
			WorldGenHelper.CHUNKLOADERS.put(world, longs = new ArrayList<>());
		if(longs.contains(chunkloader.toLong()))
			longs.remove(chunkloader.toLong());
		reloadChunks();
	}
	
	public static void reloadChunks()
	{
		LOADED_CHUNKS.clear();
		for(Integer i : CHUNKLOADERS.keySet())
			for(Long l : CHUNKLOADERS.get(i))
			{
				BlockPos pos = BlockPos.fromLong(l);
				LOADED_CHUNKS.add(new LoadableChunk(i, pos.getX() >> 4, pos.getZ() >> 4));
			}
	}
	
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
	
	public static File getBlockSaveFile(int world)
	{
		return WorldUtil.getWorldSubfile("pengu-hc_world_data-" + world + ".srd");
	}
	
	public static void setBlockState(World world, BlockPos pos, IBlockState state, @Nullable TileEntity tile)
	{
		boolean logCascade = ForgeModContainer.logCascadingWorldGeneration;
		ForgeModContainer.logCascadingWorldGeneration = false;
		
		world.setBlockState(pos, state);
		world.setTileEntity(pos, tile);
		
		ForgeModContainer.logCascadingWorldGeneration = logCascade;
	}
	
	public static void setBlockState(World world, BlockPos pos, IBlockState state)
	{
		setBlockState(world, pos, state, null);
	}
	
	public static void setBlockState(World world, BlockPos pos, IBlockState state, int marker, @Nullable TileEntity tile)
	{
		boolean logCascade = ForgeModContainer.logCascadingWorldGeneration;
		ForgeModContainer.logCascadingWorldGeneration = false;
		
		world.setBlockState(pos, state, marker);
		world.setTileEntity(pos, tile);
		
		ForgeModContainer.logCascadingWorldGeneration = logCascade;
	}
	
	public static void setBlockState(World world, BlockPos pos, IBlockState state, int marker)
	{
		setBlockState(world, pos, state, marker, null);
	}
	
	@SubscribeEvent
	public void worldSaveEvt(WorldEvent.Save evt)
	{
		try
		{
			ObjectOutputStream o = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(getBlockSaveFile(evt.getWorld().provider.getDimension()))));
			o.writeObject(CHUNKLOADERS);
			IndexedMap<String, Serializable> pars = new IndexedMap<String, Serializable>();
			MinecraftForge.EVENT_BUS.post(new WorldEventsHC.SaveData(evt.getWorld(), pars));
			o.writeObject(pars);
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
			ObjectInputStream i = new ObjectInputStream(new GZIPInputStream(new FileInputStream(getBlockSaveFile(evt.getWorld().provider.getDimension()))));
			CHUNKLOADERS.putAll((Map) i.readObject());
			IndexedMap<String, Serializable> pars = (IndexedMap<String, Serializable>) i.readObject();
			MinecraftForge.EVENT_BUS.post(new WorldEventsHC.LoadData(evt.getWorld(), pars));
			i.close();
		} catch(Throwable err)
		{
		}
	}
}