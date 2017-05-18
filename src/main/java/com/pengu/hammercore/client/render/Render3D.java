package com.pengu.hammercore.client.render;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.pengu.hammercore.client.particle.api.ParticleList;
import com.pengu.hammercore.client.render.world.PositionRenderer;

public class Render3D
{
	private static final List<PositionRenderer> renders = new ArrayList<>();
	private static final List<PositionRenderer> renderQueue = new ArrayList<>();
	
	public static void registerPositionRender(PositionRenderer render)
	{
		if(!renders.contains(render) && !renderQueue.contains(render))
			renderQueue.add(render);
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void renderWorld(RenderWorldLastEvent evt)
	{
		while(!renderQueue.isEmpty())
			renders.add(renderQueue.remove(0));
		
		for(int i = 0; i < renders.size(); ++i)
		{
			PositionRenderer render = renders.get(i);
			if(render.isDead())
				renders.remove(i);
			else if(render.canRender(Minecraft.getMinecraft().player))
				render.render(Minecraft.getMinecraft().player, render.calcX(), render.calcY(), render.calcZ());
		}
		
		ParticleList.renderExtendedParticles(evt);
	}
}