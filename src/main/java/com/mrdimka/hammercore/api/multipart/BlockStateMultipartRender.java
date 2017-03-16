package com.mrdimka.hammercore.api.multipart;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.common.blocks.multipart.TileMultipart;

/**
 * Renders a block state in the place of {@link TileMultipart}
 */
public class BlockStateMultipartRender implements IMultipartRender<MultipartSignature>
{
	@Override
	public void renderMultipartAt(MultipartSignature signature, double x, double y, double z, float partialTicks, int destroyStage)
	{
		IBlockState state = signature.getState();
		if(state != null)
		{
			Tessellator tes = Tessellator.getInstance();
			tes.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
			Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(state, signature.pos, signature.world, tes.getBuffer());
			tes.draw();
		}
	}
}