package com.mrdimka.hammercore.client.utils;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class UtilsFX
{
	private static final ResourceLocation PARTICLE_TEXTURES = new ResourceLocation("textures/particle/particles.png");
	
	public static ResourceLocation getMCParticleTexture()
	{
		return PARTICLE_TEXTURES;
	}
	
	public static void drawBeam(Vec3d S, Vec3d E, Vec3d P, float width, int bright)
	{
		drawBeam(S, E, P, width, bright, 1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	public static void drawBeam(Vec3d S, Vec3d E, Vec3d P, float width, int bright, float r, float g, float b, float a)
	{
		Vec3d PS = Sub(S, P);
		Vec3d SE = Sub(E, S);
		
		Vec3d normal = Cross(PS, SE);
		normal = normal.normalize();
		
		Vec3d half = Mul(normal, width);
		Vec3d p1 = Add(S, half);
		Vec3d p2 = Sub(S, half);
		Vec3d p3 = Add(E, half);
		Vec3d p4 = Sub(E, half);
		
		drawQuad(Tessellator.getInstance().getBuffer(), p1, p3, p4, p2, bright, r, g, b, a);
	}
	
	public static void drawQuad(VertexBuffer buf, Vec3d p1, Vec3d p2, Vec3d p3, Vec3d p4, int bright, float r, float g, float b, float a)
	{
		int j = bright >> 16 & 0xFFFF;
		int k = bright & 0xFFFF;
		buf.pos(p1.xCoord, p1.yCoord, p1.zCoord).tex(0.0D, 0.0D).lightmap(j, k).color(r, g, b, a).endVertex();
		buf.pos(p2.xCoord, p2.yCoord, p2.zCoord).tex(1.0D, 0.0D).lightmap(j, k).color(r, g, b, a).endVertex();
		buf.pos(p3.xCoord, p3.yCoord, p3.zCoord).tex(1.0D, 1.0D).lightmap(j, k).color(r, g, b, a).endVertex();
		buf.pos(p4.xCoord, p4.yCoord, p4.zCoord).tex(0.0D, 1.0D).lightmap(j, k).color(r, g, b, a).endVertex();
	}
	
	private static Vec3d Cross(Vec3d a, Vec3d b)
	{
		double xCoord = a.yCoord * b.zCoord - a.zCoord * b.yCoord;
		double yCoord = a.zCoord * b.xCoord - a.xCoord * b.zCoord;
		double zCoord = a.xCoord * b.yCoord - a.yCoord * b.xCoord;
		return new Vec3d(xCoord, yCoord, zCoord);
	}
	
	public static Vec3d Sub(Vec3d a, Vec3d b)
	{
		return new Vec3d(a.xCoord - b.xCoord, a.yCoord - b.yCoord, a.zCoord - b.zCoord);
	}
	
	private static Vec3d Add(Vec3d a, Vec3d b)
	{
		return new Vec3d(a.xCoord + b.xCoord, a.yCoord + b.yCoord, a.zCoord + b.zCoord);
	}
	
	private static Vec3d Mul(Vec3d a, float f)
	{
		return new Vec3d(a.xCoord * f, a.yCoord * f, a.zCoord * f);
	}
}