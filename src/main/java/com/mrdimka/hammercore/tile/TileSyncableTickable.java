package com.mrdimka.hammercore.tile;

import net.minecraft.util.ITickable;

public abstract class TileSyncableTickable extends TileSyncable implements ITickable
{
	public int ticksExisted = 0;
	
	@Override
	public final void update()
	{
		ticksExisted++;
		tick();
	}
	
	public void tick() {}
}