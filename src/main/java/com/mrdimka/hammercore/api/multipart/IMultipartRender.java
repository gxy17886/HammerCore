package com.mrdimka.hammercore.api.multipart;

import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.util.ResourceLocation;

import com.mrdimka.hammercore.HammerCore;

/**
 * Provides rendering of {@link MP} extends {@link MultipartSignature}. Use {@code MultipartRenderingRegistry} to bind it to {@link MultipartSignature}
 */
public interface IMultipartRender<MP extends MultipartSignature>
{
	void renderMultipartAt(MP signature, double x, double y, double z, float partialTicks, ResourceLocation destroyStage);
	
	default void bindTexture(ResourceLocation location)
	{
		HammerCore.renderProxy.bindTexture(location);
	}
}