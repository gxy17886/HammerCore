package com.pengu.hammercore.client.particle.api;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;

public class ParticleList
{
	private static final Set<IParticleGetter> getters = new HashSet<>();
	
	private static final Set<Particle> vanillaParticleSet = new HashSet<>();
	private static final List<Particle> vanillaParticleList = new ArrayList<>();
	
	static
	{
		addGetter(new DefaultParticleGetter());
	}
	
	public static void addGetter(IParticleGetter getter)
	{
		getters.add(getter);
	}
	
	public static void refreshParticles()
	{
		vanillaParticleSet.clear();
		vanillaParticleList.clear();
		
		if(Minecraft.getMinecraft().player == null || Minecraft.getMinecraft().world == null)
			return;
		
		for(IParticleGetter getter : getters)
			getter.addParticles(vanillaParticleSet);
		
		vanillaParticleList.addAll(vanillaParticleSet);
	}
	
	public static Set<Particle> getParticlesSet()
	{
		return vanillaParticleSet;
	}
	
	public static List<Particle> getParticlesList()
	{
		return vanillaParticleList;
	}
	
	private static class DefaultParticleGetter implements IParticleGetter
	{
		@Override
		public void addParticles(Set<Particle> particlesSet)
		{
			try
			{
				ArrayDeque<Particle>[][] particles = Minecraft.getMinecraft().effectRenderer.fxLayers;
				for(ArrayDeque<Particle>[] layer : particles)
					for(ArrayDeque<Particle> deque : layer)
						particlesSet.addAll(deque);
			} catch(Throwable err)
			{
			}
		}
	}
}