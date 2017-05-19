package com.mrdimka.hammercore.tile;

import com.pengu.hammercore.net.utils.NetPropertyAbstract;

import net.minecraft.util.ITickable;

public abstract class TileSyncableTickable extends TileSyncable implements ITickable
{
	public int changes = 0;
	public int ticksExisted = 0;
	
	@Override
	public final void update()
	{
		ticksExisted++;
		tick();
		
		if(changes > 0)
		{
			changes = 0;
			sendChangesToNearby();
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