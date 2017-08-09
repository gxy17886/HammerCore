package com.pengu.hammercore.client.render;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mrdimka.hammercore.client.utils.RenderUtil;
import com.mrdimka.hammercore.math.MathHelper;
import com.pengu.hammercore.client.render.world.PositionRenderer;
import com.pengu.hammercore.color.Color;
import com.pengu.hammercore.color.ColorARGB;

public class Render3D
{
	private static final ResourceLocation DEFAULT_BEAM_TEXTURE = new ResourceLocation("hammercore", "textures/misc/beaml.png");
	
	public static int ticks = 0;
	private static final String AUTHOR_USERNAME = "APengu", AUTHOR_DNAME = TextFormatting.BLUE + "" + TextFormatting.ITALIC + "       " + TextFormatting.RESET + "  ";
	
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
			else if(render.canRender(Minecraft.getMinecraft().thePlayer))
				render.render(Minecraft.getMinecraft().thePlayer, render.calcX(), render.calcY(), render.calcZ());
		}
	}
	
	@SubscribeEvent
	public void renderLiving(RenderLivingEvent.Pre<EntityLivingBase> e)
	{
		if(!(e.getEntity() instanceof AbstractClientPlayer))
			return;
		
		AbstractClientPlayer player = (AbstractClientPlayer) e.getEntity();
		
		// if(!loadedPlayers.contains(player.getGameProfile().getName()))
		// {
		// setPlayerTexture(player, new ResourceLocation("skins/" +
		// player.getGameProfile().getName()));
		// loadedPlayers.add(player.getGameProfile().getName());
		// }
	}
	
	public static int chatX = 0;
	public static int chatY = 0;
	
	@SubscribeEvent
	public void cpe(RenderGameOverlayEvent.Chat event)
	{
		chatX = event.getPosX();
		chatY = event.getPosY();
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onTick(TickEvent.ClientTickEvent event)
	{
		if(event.side == Side.CLIENT && event.phase == TickEvent.Phase.START)
			ticks++;
	}
	
	@SubscribeEvent
	public void rne(RenderGameOverlayEvent.Post event)
	{
		if(event.getType() == ElementType.CHAT)
		{
			GuiNewChat c = Minecraft.getMinecraft().ingameGUI.getChatGUI();
			
			List<ChatLine> chatLines = c.drawnChatLines;
			int updateCounter = Minecraft.getMinecraft().ingameGUI.updateCounter;
			
			for(int i = 0; c.getChatOpen() && i < chatLines.size() || !c.getChatOpen() && i < chatLines.size() && i < 10; i++)
			{
				ChatLine l = chatLines.get(i);
				String s = l.getChatComponent().getUnformattedText();
				for(int j = 0; j < s.length(); j++)
				{
					if(j < s.length() - AUTHOR_DNAME.length() && s.substring(j, j + AUTHOR_DNAME.length()).equals(AUTHOR_DNAME))
					{
						String before = s.substring(0, j);
						float f = Minecraft.getMinecraft().gameSettings.chatOpacity * .9F + .1F;
						int j1 = updateCounter - l.getUpdatedCounter();
						if(j1 < 200 || c.getChatOpen())
						{
							double d0 = (double) j1 / 200.0D;
							d0 = 1.0D - d0;
							d0 = d0 * 10.0D;
							d0 = MathHelper.clip(d0, 0, 1);
							d0 = d0 * d0;
							int l1 = (int) (255D * d0);
							
							if(c.getChatOpen())
								l1 = 255;
							
							l1 = (int) ((float) l1 * f);
							if((20 * l1) / 255 > 3)
							{
								GlStateManager.enableAlpha();
								GlStateManager.enableBlend();
								GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
								int dfunc = GL11.glGetInteger(GL11.GL_DEPTH_FUNC);
								GlStateManager.depthFunc(GL11.GL_LEQUAL);
								int func = GL11.glGetInteger(GL11.GL_ALPHA_TEST_FUNC);
								float ref = GL11.glGetFloat(GL11.GL_ALPHA_TEST_REF);
								GlStateManager.alphaFunc(GL11.GL_ALWAYS, 0);
								GlStateManager.depthMask(false);
								
								GL11.glTranslated(.25, 0, 0);
								drawTextGlowingAuraTransparent(Minecraft.getMinecraft().fontRendererObj, AUTHOR_USERNAME, chatX + 2 + Minecraft.getMinecraft().fontRendererObj.getStringWidth(before), chatY - (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT) * i, l1);
								GL11.glTranslated(-.25, 0, 0);
								
								GlStateManager.depthMask(true);
								GlStateManager.alphaFunc(func, ref);
								GlStateManager.depthFunc(dfunc);
								GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
								GlStateManager.disableBlend();
								GlStateManager.disableAlpha();
							}
						}
					}
				}
			}
			
		}
	}
	
	@SubscribeEvent
	public void onNameFormat(PlayerEvent.NameFormat event)
	{
		if(event.getUsername().equals(AUTHOR_USERNAME))
			event.setDisplayname(AUTHOR_DNAME);
	}
	
	public static void drawTextGlowingAuraTransparent(FontRenderer font, String s, int x, int y, int a)
	{
		float sine = .5F * ((float) Math.sin(Math.toRadians(4 * ticks + Minecraft.getMinecraft().getRenderPartialTicks())) + 1);
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE);
		
		int r = 16;
		int g = 16;
		int b = 175 + (int) (80 * sine);
		
		font.drawString(s, x, y, Color.packARGB(r, g, b, a));
		RenderUtil.drawTextRGBA(font, s, x - 1, y, r, g, b, (40 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x + 1, y, r, g, b, (40 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x, y - 1, r, g, b, (40 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x, y + 1, r, g, b, (40 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x - 2, y, r, g, b, (20 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x + 2, y, r, g, b, (20 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x, y - 2, r, g, b, (20 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x, y + 2, r, g, b, (20 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x - 1, y + 1, r, g, b, (20 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x + 1, y - 1, r, g, b, (20 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x - 1, y - 1, r, g, b, (20 * a) / 255);
		RenderUtil.drawTextRGBA(font, s, x + 1, y + 1, r, g, b, (20 * a) / 255);
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
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
	
	public static void drawLine(Vec3d start, Vec3d end, int color, float size)
	{
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		ColorARGB.glColourRGBA(color);
		GL11.glPushMatrix();
		GL11.glLineWidth(size);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(start.xCoord, start.yCoord, start.zCoord);
		GL11.glVertex3d(end.xCoord, end.yCoord, end.zCoord);
		GL11.glEnd();
		GL11.glPopMatrix();
		GlStateManager.enableTexture2D();
		ColorARGB.glColourRGBA(0xFFFFFFFF);
	}
}