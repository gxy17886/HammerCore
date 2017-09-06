package com.pengu.hammercore.world.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.pengu.hammercore.annotations.MCFBus;
import com.pengu.hammercore.event.WorldEventsHC;

@MCFBus
public class WorldRetroGen
{
	private static final Map<Integer, HashMap<String, HashMap<Long, Boolean>>> retrogenerations = new HashMap<>();
	private static final List<String> mods = new ArrayList<>();
	private static final Map<String, List<IWorldGenerator>> generators = new HashMap<>();
	private static final Map<String, List<iWorldGenFeature>> features = new HashMap<>();
	private static final Set<String> populating = new HashSet<>();
	
	public static void addWorldGenerator(IWorldGenerator gen)
	{
		String mod = Loader.instance().activeModContainer().getModId();
		if(!mods.contains(mod))
			mods.add(mod);
		List<IWorldGenerator> gens = generators.get(mod);
		if(gens == null)
			generators.put(mod, gens = new ArrayList<>());
		gens.add(gen);
	}
	
	public static void addWorldFeature(iWorldGenFeature gen)
	{
		String mod = Loader.instance().activeModContainer().getModId();
		if(!mods.contains(mod))
			mods.add(mod);
		List<iWorldGenFeature> gens = features.get(mod);
		if(gens == null)
			features.put(mod, gens = new ArrayList<>());
		gens.add(gen);
	}
	
	@SubscribeEvent
	public void worldLoad(WorldEventsHC.LoadData evt)
	{
		retrogenerations.put(evt.getWorld().provider.getDimension(), (HashMap<String, HashMap<Long, Boolean>>) evt.additionalData.get("HCRetroGenStuff"));
	}
	
	@SubscribeEvent
	public void worldSave(WorldEventsHC.SaveData evt)
	{
		HashMap<String, HashMap<Long, Boolean>> map = retrogenerations.get(evt.getWorld().provider.getDimension());
		if(map == null)
			map = new HashMap<String, HashMap<Long, Boolean>>();
		evt.additionalData.put("HCRetroGenStuff", map);
	}
	
	public static void clearCache()
	{
		retrogenerations.clear();
	}
	
	public static void generateChunk(Chunk c)
	{
		String key = c.getWorld().provider.getDimension() + "," + c.x + "," + c.z;
		
		if(populating.contains(key))
			return;
		populating.add(key);
		
		HashMap<String, HashMap<Long, Boolean>> map = retrogenerations.get(c.getWorld().provider.getDimension());
		if(map == null)
			retrogenerations.put(c.getWorld().provider.getDimension(), map = new HashMap<String, HashMap<Long, Boolean>>());
		
		Random random = new Random(c.getWorld().getSeed());
		long xSeed = random.nextLong() >> 2 + 1L;
		long zSeed = random.nextLong() >> 2 + 1L;
		long chunkSeed = (xSeed * c.x + zSeed * c.z) ^ c.getWorld().getSeed();
		
		IChunkGenerator cgen = null;
		
		for(String mod : mods)
		{
			List<IWorldGenerator> gens = WorldRetroGen.generators.get(mod);
			List<iWorldGenFeature> features = WorldRetroGen.features.get(mod);
			
			HashMap<Long, Boolean> generated = map.get(mod);
			if(generated == null)
				map.put(mod, generated = new HashMap<>());
			
			long gen = ChunkPos.asLong(c.x, c.z);
			if(generated.get(gen) != Boolean.TRUE)
			{
				generated.put(gen, true);
				
				random.setSeed(chunkSeed);
				if(gens != null)
					for(IWorldGenerator igen : gens)
					{
						if(cgen == null)
							cgen = c.getWorld().provider.createChunkGenerator();
						igen.generate(random, c.x, c.z, c.getWorld(), cgen, c.getWorld().getChunkProvider());
					}
				
				random.setSeed(chunkSeed);
				if(features != null)
					for(iWorldGenFeature feat : features)
					{
						ChunkPos cp = new ChunkPos(c.x, c.z);
						for(int i = 0; i < random.nextInt(feat.getMaxChances(c.getWorld(), cp, random)); ++i)
						{
							int genX = random.nextInt(16) + c.x * 16;
							int genZ = random.nextInt(16) + c.z * 16;
							BlockPos pos = new BlockPos(genX, 0, genZ);
							int minY = feat.getMinY(c.getWorld(), pos, random);
							int yDelta = feat.getMaxY(c.getWorld(), pos, random) - minY;
							pos = new BlockPos(genX, random.nextInt(yDelta) + minY, genZ);
							feat.generate(c.getWorld(), pos, random);
						}
					}
			}
		}
		
		populating.remove(key);
	}
}