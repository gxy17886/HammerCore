package com.pengu.hammercore.client.particle.api;

import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerX;
import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerY;
import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerZ;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mrdimka.hammercore.common.utils.WorldUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ParticleList
{
	private static final Set<IParticleGetter> getters = new HashSet<>();
	
	private static final Set<Particle> vanillaParticleSet = new HashSet<>();
	private static final List<Particle> vanillaParticleList = new ArrayList<>();
	
	private static final List<IRenderedParticle> renderedParticleList = new ArrayList<>();
	
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
		renderedParticleList.clear();
		
		if(Minecraft.getMinecraft().player == null || Minecraft.getMinecraft().world == null)
			return;
		
		for(IParticleGetter getter : getters)
			getter.addParticles(vanillaParticleSet);
		
		vanillaParticleList.addAll(vanillaParticleSet);
		for(int i = 0; i < vanillaParticleList.size(); ++i)
		{
			Particle p = vanillaParticleList.get(i);
			if(p instanceof IRenderedParticle)
				renderedParticleList.add((IRenderedParticle) p);
		}
	}
	
	public static void renderExtendedParticles(RenderWorldLastEvent evt)
	{
		for(int i = 0; i < renderedParticleList.size(); ++i)
		{
			Particle p = (Particle) renderedParticleList.get(i);
			IRenderedParticle rp = WorldUtil.cast(p, IRenderedParticle.class);
			if(rp != null)
				rp.doRenderParticle(p.posX - staticPlayerX, p.posY - staticPlayerY, p.posZ - staticPlayerZ, evt.getPartialTicks());
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
	
	public static List<IRenderedParticle> getRenderedParticleList()
    {
	    return renderedParticleList;
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