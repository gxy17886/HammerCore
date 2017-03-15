package com.mrdimka.hammercore.client.renderer.tile;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.api.multipart.IMultipartRender;
import com.mrdimka.hammercore.api.multipart.MultipartRenderingRegistry;
import com.mrdimka.hammercore.api.multipart.MultipartSignature;
import com.mrdimka.hammercore.common.blocks.multipart.TileMultipart;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;

public class TileRenderMultipart extends TileEntitySpecialRenderer<TileMultipart>
{
	@Override
	public void renderTileEntityAt(TileMultipart te, double x, double y, double z, float partialTicks, int destroyStage)
	{
		for(MultipartSignature s : te.signatures())
		{
			IMultipartRender render = MultipartRenderingRegistry.getRender(s);
			GL11.glPushMatrix();
			if(render != null) render.renderMultipartAt(s, x, y, z, partialTicks, destroyStage);
			GL11.glPopMatrix();
		}
	}
}