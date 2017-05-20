package com.pengu.hammercore.client.particle.api;

import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerX;
import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerY;
import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerZ;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import com.pengu.hammercore.client.particle.api.common.ExtendedParticle;

public class ParticleList
{
	private static final Set<IParticleGetter> getters = new HashSet<>();
	
	private static final Set<Particle> vanillaParticleSet = new HashSet<>();
	private static final List<Particle> vanillaParticleList = new ArrayList<>();
	
	private static final Map<UUID, ExtendedParticle> extendedParticleMap = new HashMap<>();
	private static final List<ExtendedParticle> extendedParticleList = new ArrayList<>();
	
	private static final Map<Class<?>, ParticleRenderer> renderers = new HashMap<>();
	
	static
	{
		addGetter(new DefaultParticleGetter());
	}
	
	public static <T extends ExtendedParticle> void registerRenderer(Class<T> particleClass, ParticleRenderer<T> renderer)
	{
		renderers.put(particleClass, renderer);
	}
	
	public static <T extends ExtendedParticle> ParticleRenderer<T> getRenderer(Class<T> particleClass)
	{
		return renderers.get(particleClass);
	}
	
	public static void addGetter(IParticleGetter getter)
	{
		getters.add(getter);
	}
	
	public static ExtendedParticle getExtendedParticle(UUID id)
	{
		return extendedParticleMap.get(id);
	}
	
	public static void spawnExtendedParticle(ExtendedParticle e)
	{
		if(getExtendedParticle(e.getUUID()) != null)
			return;
		extendedParticleMap.put(e.getUUID(), e);
		extendedParticleList.add(e);
	}
	
	public static void refreshParticles()
	{
		vanillaParticleSet.clear();
		vanillaParticleList.clear();
		extendedParticleList.clear();
		
		if(Minecraft.getMinecraft().player == null || Minecraft.getMinecraft().world == null)
			return;
		
		for(IParticleGetter getter : getters)
			getter.addParticles(vanillaParticleSet);
		
		vanillaParticleList.addAll(vanillaParticleSet);
		extendedParticleList.addAll(extendedParticleMap.values());
		
		for(int i = 0; i < extendedParticleList.size(); ++i)
		{
			ExtendedParticle p = extendedParticleList.get(i);
			p.update();
			if(p.isDead.get())
				extendedParticleList.remove(i);
		}
	}
	
	public static void renderExtendedParticles(RenderWorldLastEvent evt)
	{
		for(int i = 0; i < extendedParticleList.size(); ++i)
		{
			ExtendedParticle p = extendedParticleList.get(i);
			ParticleRenderer r = getRenderer(p.getClass());
			if(r != null)
				r.doRender(p, p.posX.get() - staticPlayerX, p.posY.get() - staticPlayerY, p.posZ.get() - staticPlayerZ, evt.getPartialTicks());
		}
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