package com.pengu.hammercore.client.particle.api.common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

import com.mrdimka.hammercore.annotations.MCFBus;

@MCFBus
public class ExtendedParticleTicker
{
	private static final List<ExtendedParticle> PARTICLES = new ArrayList<>();
	
	public static void spawnParticle(ExtendedParticle ep)
	{
		if(PARTICLES.contains(ep))
			return;
		PARTICLES.add(ep);
		ep.sync();
	}
	
	@SubscribeEvent
	public void serverTick(ServerTickEvent ste)
	{
		for(int i = 0; i < PARTICLES.size(); ++i)
		{
			ExtendedParticle ep = PARTICLES.get(i);
			ep.update();
			if(ep.isDead.get())
				PARTICLES.remove(i);
		}
	}
}