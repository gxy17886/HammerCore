package com.pengu.hammercore.common.chunk;

import java.util.ArrayList;

import net.minecraft.world.World;

import com.mrdimka.hammercore.api.IUpdatable;
import com.pengu.hammercore.common.chunk.ChunkPredicate.IChunkLoader;
import com.pengu.hammercore.common.chunk.ChunkPredicate.LoadableChunk;

public enum ChunkLoaderHC implements IChunkLoader, IUpdatable
{
	INSTANCE;
	
	private final ArrayList<LoadableChunk> chunks = new ArrayList<>();
	private final ArrayList<Long> chunkTimers = new ArrayList<>();
	
	public void registerChunkWithTimeout(LoadableChunk chunk, long timer)
	{
		chunks.add(chunk);
		chunkTimers.add(timer);
	}
	
	@Override
	public void update()
	{
		for(int i = 0; i < chunks.size(); ++i)
		{
			long ticksLeft = chunkTimers.get(i);
			
			if(ticksLeft != -1L && ticksLeft > 0L)
			{
				ticksLeft--;
				chunkTimers.set(i, ticksLeft);
			}
			
			if(ticksLeft == 0L)
			{
				chunks.remove(i);
				chunkTimers.remove(i);
			}
		}
	}
	
	@Override
	public boolean isAlive()
	{
		return true;
	}
	
	@Override
	public boolean shouldChunkBeLoaded(World world, int x, int z)
	{
		return false;
	}
	
	@Override
	public ArrayList<LoadableChunk> getForceLoadedChunks()
	{
		return chunks;
	}
}