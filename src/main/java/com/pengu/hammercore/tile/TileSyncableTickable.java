package com.pengu.hammercore.tile;

import net.minecraft.util.ITickable;

import com.pengu.hammercore.net.utils.NetPropertyAbstract;

public abstract class TileSyncableTickable extends TileSyncable implements ITickable
{
	public int changes = 0;
	public int ticksExisted = 0;
	
	@Override
	public final void update()
	{
		/* Updates location each tick, if needed. */
		getLocation();
		
		ticksExisted++;
		tick();
		
		if(changes > 0)
		{
			changes = 0;
			sync();
		}
	}
	
	public void tick()
	{
	}
	
	@Override
	public void notifyOfChange(NetPropertyAbstract prop)
	{
		changes++;
	}
	
	@Override
	public void sendChangesToNearby()
	{
		changes++;
	}
}