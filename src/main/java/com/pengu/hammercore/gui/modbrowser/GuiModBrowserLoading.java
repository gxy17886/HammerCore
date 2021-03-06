package com.pengu.hammercore.gui.modbrowser;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import org.lwjgl.util.glu.Project;

import com.pengu.hammercore.common.HolidayTrigger;
import com.pengu.hammercore.common.utils.DynamicObject;
import com.pengu.hammercore.modbrowser.ModBrowser;

public class GuiModBrowserLoading extends GuiScreen
{
	private DynamicTexture viewportTexture;
	private static final ResourceLocation[] TITLE_PANORAMA_PATHS = new ResourceLocation[] { new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png") };
	private ResourceLocation backgroundTexture;
	public float panoramaTimer;
	
	public final DynamicObject<String> status = new DynamicObject<String>("Starting...");
	
	@Override
	public void initGui()
	{
		super.initGui();
		this.viewportTexture = new DynamicTexture(256, 256);
		this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		GlStateManager.disableAlpha();
		this.renderSkybox(mouseX, mouseY, partialTicks);
		GlStateManager.enableAlpha();
		
		this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
		this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
		
		String splashText = "this is a WIP mod browser for Pengu's mods";
		
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) (this.width / 2), 70.0F, 0.0F);
		float f = 1.8F - MathHelper.abs(MathHelper.sin((float) ((Minecraft.getSystemTime() + splashText.hashCode()) % 1000L) / 1000.0F * ((float) Math.PI * 2F)) * 0.1F);
		f = f * 100.0F / (float) (this.fontRenderer.getStringWidth(splashText) + 32);
		GlStateManager.scale(f, f, f);
		this.drawCenteredString(this.fontRenderer, splashText, 0, -8, splashText.hashCode());
		GlStateManager.popMatrix();
		
		splashText = status.get();
		
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) (this.width / 2), 90.0F, 0.0F);
		f = 1.8F - MathHelper.abs(MathHelper.sin((float) ((Minecraft.getSystemTime() + splashText.hashCode()) % 1000L) / 1000.0F * ((float) Math.PI * 2F)) * 0.1F);
		f = f * 100.0F / (float) (this.fontRenderer.getStringWidth(splashText) + 32);
		GlStateManager.scale(f, f, f);
		this.drawCenteredString(this.fontRenderer, splashText, 0, -8, splashText.hashCode());
		GlStateManager.popMatrix();
		
		splashText = "press ESC to exit this menu";
		
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) (this.width / 2), 110.0F, 0.0F);
		f = 1.8F - MathHelper.abs(MathHelper.sin((float) ((Minecraft.getSystemTime() + splashText.hashCode()) % 1000L) / 1000.0F * ((float) Math.PI * 2F)) * 0.1F);
		f = f * 100.0F / (float) (this.fontRenderer.getStringWidth(splashText) + 32);
		GlStateManager.scale(f, f, f);
		this.drawCenteredString(this.fontRenderer, splashText, 0, -8, splashText.hashCode());
		GlStateManager.popMatrix();
		
		splashText = HolidayTrigger.isAprilFools() ? "U mad bro?" : HolidayTrigger.isChristmas() ? "Mery Christmas!" : HolidayTrigger.isHalloween() ? "Ooo! Spooky!" : HolidayTrigger.isNewYear() ? "Happy New Year!" : "Loading? Not really.";
		
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) (this.width / 2), 130.0F, 0.0F);
		f = 1.8F - MathHelper.abs(MathHelper.sin((float) ((Minecraft.getSystemTime() + splashText.hashCode()) % 1000L) / 1000.0F * ((float) Math.PI * 2F)) * 0.1F);
		f = f * 100.0F / (float) (this.fontRenderer.getStringWidth(splashText) + 32);
		GlStateManager.scale(f, f, f);
		this.drawCenteredString(this.fontRenderer, splashText, 0, -8, splashText.hashCode());
		GlStateManager.popMatrix();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	private boolean startsLoading = false;
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		panoramaTimer += 8;
		
		if(!startsLoading)
		{
			startsLoading = true;
			new Thread(() ->
			{
				try
				{
					ModBrowser browser = new ModBrowser(status);
					Minecraft.getMinecraft().addScheduledTask(() ->
					{
						Minecraft.getMinecraft().displayGuiScreen(new GuiModBrowser(browser));
					});
				} catch(Exception e)
				{
					e.printStackTrace();
					status.set("Failed to load mod browser!");
					
					try
					{
						Thread.sleep(5000L);
					} catch(Throwable err)
					{
					}
					
					Minecraft.getMinecraft().addScheduledTask(() ->
					{
						GuiMainMenu gui;
						Minecraft.getMinecraft().displayGuiScreen(gui = new GuiMainMenu());
						gui.panoramaTimer = panoramaTimer;
					});
				}
			}).start();
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(keyCode == 1)
		{
			GuiMainMenu gui;
			mc.displayGuiScreen(gui = new GuiMainMenu());
			gui.panoramaTimer = panoramaTimer;
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
	}
	
	private void drawPanorama(int mouseX, int mouseY, float partialTicks)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		GlStateManager.matrixMode(5889);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
		GlStateManager.matrixMode(5888);
		GlStateManager.pushMatrix();
		GlStateManager.loadIdentity();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.disableCull();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		int i = 8;
		
		for(int j = 0; j < 64; ++j)
		{
			GlStateManager.pushMatrix();
			float f = ((float) (j % 8) / 8.0F - 0.5F) / 64.0F;
			float f1 = ((float) (j / 8) / 8.0F - 0.5F) / 64.0F;
			float f2 = 0.0F;
			GlStateManager.translate(f, f1, 0.0F);
			GlStateManager.rotate(MathHelper.sin(((float) this.panoramaTimer + partialTicks) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
			GlStateManager.rotate(-((float) this.panoramaTimer + partialTicks) * 0.1F, 0.0F, 1.0F, 0.0F);
			
			for(int k = 0; k < 6; ++k)
			{
				GlStateManager.pushMatrix();
				
				if(k == 1)
				{
					GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
				}
				
				if(k == 2)
				{
					GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				}
				
				if(k == 3)
				{
					GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
				}
				
				if(k == 4)
				{
					GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				}
				
				if(k == 5)
				{
					GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
				}
				
				this.mc.getTextureManager().bindTexture(TITLE_PANORAMA_PATHS[k]);
				vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
				int l = 255 / (j + 1);
				float f3 = 0.0F;
				vertexbuffer.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, l).endVertex();
				vertexbuffer.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, l).endVertex();
				vertexbuffer.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, l).endVertex();
				vertexbuffer.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, l).endVertex();
				tessellator.draw();
				GlStateManager.popMatrix();
			}
			
			GlStateManager.popMatrix();
			GlStateManager.colorMask(true, true, true, false);
		}
		
		vertexbuffer.setTranslation(0.0D, 0.0D, 0.0D);
		GlStateManager.colorMask(true, true, true, true);
		GlStateManager.matrixMode(5889);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(5888);
		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.enableDepth();
	}
	
	private void rotateAndBlurSkybox(float partialTicks)
	{
		this.mc.getTextureManager().bindTexture(this.backgroundTexture);
		GlStateManager.glTexParameteri(3553, 10241, 9729);
		GlStateManager.glTexParameteri(3553, 10240, 9729);
		GlStateManager.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.colorMask(true, true, true, false);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		GlStateManager.disableAlpha();
		int i = 3;
		
		for(int j = 0; j < 3; ++j)
		{
			float f = 1.0F / (float) (j + 1);
			int k = this.width;
			int l = this.height;
			float f1 = (float) (j - 1) / 256.0F;
			vertexbuffer.pos((double) k, (double) l, (double) this.zLevel).tex((double) (0.0F + f1), 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			vertexbuffer.pos((double) k, 0.0D, (double) this.zLevel).tex((double) (1.0F + f1), 1.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			vertexbuffer.pos(0.0D, 0.0D, (double) this.zLevel).tex((double) (1.0F + f1), 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
			vertexbuffer.pos(0.0D, (double) l, (double) this.zLevel).tex((double) (0.0F + f1), 0.0D).color(1.0F, 1.0F, 1.0F, f).endVertex();
		}
		
		tessellator.draw();
		GlStateManager.enableAlpha();
		GlStateManager.colorMask(true, true, true, true);
	}
	
	private void renderSkybox(int mouseX, int mouseY, float partialTicks)
	{
		this.mc.getFramebuffer().unbindFramebuffer();
		GlStateManager.viewport(0, 0, 256, 256);
		this.drawPanorama(mouseX, mouseY, partialTicks);
		this.rotateAndBlurSkybox(partialTicks);
		this.rotateAndBlurSkybox(partialTicks);
		this.rotateAndBlurSkybox(partialTicks);
		this.rotateAndBlurSkybox(partialTicks);
		this.rotateAndBlurSkybox(partialTicks);
		this.rotateAndBlurSkybox(partialTicks);
		this.rotateAndBlurSkybox(partialTicks);
		this.mc.getFramebuffer().bindFramebuffer(true);
		GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		float f = 120.0F / (float) (this.width > this.height ? this.width : this.height);
		float f1 = (float) this.height * f / 256.0F;
		float f2 = (float) this.width * f / 256.0F;
		int i = this.width;
		int j = this.height;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		vertexbuffer.pos(0.0D, (double) j, (double) this.zLevel).tex((double) (0.5F - f1), (double) (0.5F + f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		vertexbuffer.pos((double) i, (double) j, (double) this.zLevel).tex((double) (0.5F - f1), (double) (0.5F - f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		vertexbuffer.pos((double) i, 0.0D, (double) this.zLevel).tex((double) (0.5F + f1), (double) (0.5F - f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		vertexbuffer.pos(0.0D, 0.0D, (double) this.zLevel).tex((double) (0.5F + f1), (double) (0.5F + f2)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
		tessellator.draw();
	}
}