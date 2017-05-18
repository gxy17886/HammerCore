package com.pengu.hammercore.client.render;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.pengu.hammercore.client.particle.api.ParticleList;
import com.pengu.hammercore.client.render.world.PositionRenderer;

public class Render3D
{
	private static final List<PositionRenderer> renders = new ArrayList<>();
	private static final List<PositionRenderer> renderQueue = new ArrayList<>();
	
	public static final Set<String> loadedPlayers = new HashSet<>();
	
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
	
	@SubscribeEvent
	public void renderLiving(RenderLivingEvent.Pre<EntityLivingBase> e)
	{
		if(!(e.getEntity() instanceof AbstractClientPlayer))
			return;
		
		AbstractClientPlayer player = (AbstractClientPlayer) e.getEntity();
		
//		if(!loadedPlayers.contains(player.getGameProfile().getName()))
//		{
//			setPlayerTexture(player, new ResourceLocation("skins/" + player.getGameProfile().getName()));
//			loadedPlayers.add(player.getGameProfile().getName());
//		}
	}
	
	private void setPlayerTexture(AbstractClientPlayer player, ResourceLocation texture)
	{
		NetworkPlayerInfo playerInfo = (NetworkPlayerInfo) ObfuscationReflectionHelper.getPrivateValue(AbstractClientPlayer.class, player, 0);
		if(playerInfo == null)
			return;
		Map<MinecraftProfileTexture.Type, ResourceLocation> playerTextures = (Map) ObfuscationReflectionHelper.getPrivateValue(NetworkPlayerInfo.class, playerInfo, 1);
		playerTextures.put(MinecraftProfileTexture.Type.SKIN, texture);
		if(texture == null)
			ObfuscationReflectionHelper.setPrivateValue(NetworkPlayerInfo.class, playerInfo, false, 4);
	}
}