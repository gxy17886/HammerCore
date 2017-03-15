package com.mrdimka.hammercore.api.multipart;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.common.blocks.multipart.TileMultipart;

import net.minecraft.util.ResourceLocation;

public interface IMultipartRender<MP extends MultipartSignature>
{
	void renderMultipartAt(MP signature, double x, double y, double z, float partialTicks, int destroyStage);
	
	default void bindTexture(ResourceLocation location)
	{
		HammerCore.renderProxy.bindTexture(location);
	}
}