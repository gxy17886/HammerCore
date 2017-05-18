package com.pengu.hammercore.client.particle.api;

import net.minecraft.client.renderer.VertexBuffer;

import com.pengu.hammercore.client.particle.api.common.ExtendedParticle;

public abstract class ParticleRenderer<T extends ExtendedParticle>
{
	public abstract void doRender(T particle, double x, double y, double z, float partialTicks);
	
//	public void renderFast(T particle, double x, double y, double z, float partialTicks, VertexBuffer buf) {};
//	public boolean canRenderFast(T particle)
//	{
//		return false;
//	}
}