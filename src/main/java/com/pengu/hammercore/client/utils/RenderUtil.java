package com.pengu.hammercore.client.utils;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.color.Color;
import com.pengu.hammercore.color.ColorARGB;
import com.pengu.hammercore.vec.Vector3;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUtil
{
	public static double zLevel = 0;
	
	public static void drawTexturedModalRect(double x, double y, double texX, double texY, double width, double height)
	{
		float n = 0.00390625F;
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder vb = tess.getBuffer();
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);
		vb.pos(x, y + height, zLevel).tex(texX * n, (texY + height) * n).endVertex();
		vb.pos(x + width, y + height, zLevel).tex((texX + width) * n, (texY + height) * n).endVertex();
		vb.pos(x + width, y, zLevel).tex((texX + width) * n, texY * n).endVertex();
		vb.pos(x, y, zLevel).tex(texX * n, texY * n).endVertex();
		tess.draw();
	}
	
	public static void drawTexturedModalRect(double x, double y, double texX, double texY, double width, double height, double zLevel)
	{
		float n = 0.00390625F;
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder vb = tess.getBuffer();
		vb.begin(7, DefaultVertexFormats.POSITION_TEX);
		vb.pos(x, y + height, zLevel).tex(texX * n, (texY + height) * n).endVertex();
		vb.pos(x + width, y + height, zLevel).tex((texX + width) * n, (texY + height) * n).endVertex();
		vb.pos(x + width, y, zLevel).tex((texX + width) * n, texY * n).endVertex();
		vb.pos(x, y, zLevel).tex(texX * n, texY * n).endVertex();
		tess.draw();
	}
	
	public static void drawTexturedModalRect(double xCoord, double yCoord, TextureAtlasSprite textureSprite, double widthIn, double heightIn)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos(xCoord, yCoord + heightIn, 0).tex((double) textureSprite.getMinU(), (double) textureSprite.getMaxV()).endVertex();
		vertexbuffer.pos(xCoord + widthIn, yCoord + heightIn, 0).tex((double) textureSprite.getMaxU(), (double) textureSprite.getMaxV()).endVertex();
		vertexbuffer.pos(xCoord + widthIn, yCoord, 0).tex((double) textureSprite.getMaxU(), (double) textureSprite.getMinV()).endVertex();
		vertexbuffer.pos(xCoord, yCoord, 0).tex((double) textureSprite.getMinU(), (double) textureSprite.getMinV()).endVertex();
		tessellator.draw();
	}
	
	public static void drawGradientRect(double left, double top, double width, double height, int startColor, int endColor)
	{
		float f = (float) (startColor >> 24 & 255) / 255F;
		float f1 = (float) (startColor >> 16 & 255) / 255F;
		float f2 = (float) (startColor >> 8 & 255) / 255F;
		float f3 = (float) (startColor & 255) / 255F;
		float f4 = (float) (endColor >> 24 & 255) / 255F;
		float f5 = (float) (endColor >> 16 & 255) / 255F;
		float f6 = (float) (endColor >> 8 & 255) / 255F;
		float f7 = (float) (endColor & 255) / 255F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		vertexbuffer.pos(left + width, top, zLevel).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos(left, top, zLevel).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos(left, top + height, zLevel).color(f5, f6, f7, f4).endVertex();
		vertexbuffer.pos(left + width, top + height, zLevel).color(f5, f6, f7, f4).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}
	
	public static void drawGradientRect(double left, double top, double width, double height, int startColor, int endColor, double zLevel)
	{
		float f = (float) (startColor >> 24 & 255) / 255F;
		float f1 = (float) (startColor >> 16 & 255) / 255F;
		float f2 = (float) (startColor >> 8 & 255) / 255F;
		float f3 = (float) (startColor & 255) / 255F;
		float f4 = (float) (endColor >> 24 & 255) / 255F;
		float f5 = (float) (endColor >> 16 & 255) / 255F;
		float f6 = (float) (endColor >> 8 & 255) / 255F;
		float f7 = (float) (endColor & 255) / 255F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		vertexbuffer.pos(left + width, top, zLevel).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos(left, top, zLevel).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos(left, top + height, zLevel).color(f5, f6, f7, f4).endVertex();
		vertexbuffer.pos(left + width, top + height, zLevel).color(f5, f6, f7, f4).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}
	
	public static void drawTextRGBA(FontRenderer font, String s, int x, int y, int r, int g, int b, int a)
	{
		font.drawString(s, x, y, Color.packARGB(r, g, b, a));
	}
	
	public static void drawLine(Vector3 start, Vector3 end, int color, float size)
	{
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		ColorARGB.glColourRGBA(color);
		GL11.glPushMatrix();
		GL11.glLineWidth(size);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(start.x, start.y, start.z);
		GL11.glVertex3d(end.x, end.y, end.z);
		GL11.glEnd();
		GL11.glPopMatrix();
		GlStateManager.enableTexture2D();
		ColorARGB.glColourRGBA(0xFFFFFFFF);
	}
	
	public static void drawBrokenLine(int color, float size, Vector3... points)
	{
		GlStateManager.enableAlpha();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		ColorARGB.glColourRGBA(color);
		GL11.glPushMatrix();
		GL11.glLineWidth(size);
		GL11.glBegin(GL11.GL_LINES);
		for(Vector3 point : points)
			GL11.glVertex3d(point.x, point.y, point.z);
		GL11.glEnd();
		GL11.glPopMatrix();
		GlStateManager.enableTexture2D();
		ColorARGB.glColourRGBA(0xFFFFFFFF);
	}
}