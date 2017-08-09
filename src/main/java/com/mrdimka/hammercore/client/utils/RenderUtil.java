package com.mrdimka.hammercore.client.utils;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.hammercore.color.Color;

@SideOnly(Side.CLIENT)
public class RenderUtil
{
	public static double zLevel = 0;
	
	public static void drawTexturedModalRect(double x, double y, double texX, double texY, double width, double height)
	{
		float n = 0.00390625F;
		Tessellator tess = Tessellator.getInstance();
		VertexBuffer vb = tess.getBuffer();
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
		VertexBuffer vb = tess.getBuffer();
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
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos(xCoord, yCoord + heightIn, 0).tex((double) textureSprite.getMinU(), (double) textureSprite.getMaxV()).endVertex();
		vertexbuffer.pos(xCoord + widthIn, yCoord + heightIn, 0).tex((double) textureSprite.getMaxU(), (double) textureSprite.getMaxV()).endVertex();
		vertexbuffer.pos(xCoord + widthIn, yCoord, 0).tex((double) textureSprite.getMaxU(), (double) textureSprite.getMinV()).endVertex();
		vertexbuffer.pos(xCoord, yCoord, 0).tex((double) textureSprite.getMinU(), (double) textureSprite.getMinV()).endVertex();
		tessellator.draw();
	}
	
	public static void drawTextRGBA(FontRenderer font, String s, int x, int y, int r, int g, int b, int a)
	{
		font.drawString(s, x, y, Color.packARGB(r, g, b, a));
	}
}