package com.mrdimka.hammercore.api.multipart;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.common.blocks.multipart.TileMultipart;

import net.minecraft.util.ResourceLocation;

/**
 * Provides rendering of {@link MP} extends {@link MultipartSignature}. Use {@code MultipartRenderingRegistry} to bind it to {@link MultipartSignature}
 */
public interface IMultipartRender<MP extends MultipartSignature>
{
	@Deprecated
	default void renderMultipartAt(MP signature, double x, double y, double z, float partialTicks, int destroyStage) {};
	void renderMultipartAt(MP signature, double x, double y, double z, float partialTicks, ResourceLocation destroyStage);
	
	default void bindTexture(ResourceLocation location)
	{
		HammerCore.renderProxy.bindTexture(location);
	}
}