package com.mrdimka.hammercore.client.renderer.tile;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.stream.Stream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.api.multipart.IMultipartRender;
import com.mrdimka.hammercore.api.multipart.MultipartRenderingRegistry;
import com.mrdimka.hammercore.api.multipart.MultipartSignature;
import com.mrdimka.hammercore.common.blocks.multipart.BlockMultipart;
import com.mrdimka.hammercore.common.blocks.multipart.TileMultipart;
import com.mrdimka.hammercore.init.ModBlocks;
import com.mrdimka.hammercore.vec.Cuboid6;

public class TileRenderMultipart extends TileEntitySpecialRenderer<TileMultipart>
{
	@Override
	public void renderTileEntityAt(TileMultipart te, double x, double y, double z, float partialTicks, int destroyStage)
	{
//		if(te.VertexBuffer == null) te.VertexBuffer = new VertexBuffer(2_000);
		
		Stream<MultipartSignature> mps = te.signatures().stream();
		if(mps == null) return;
		
		ResourceLocation destroy = null;
		RayTraceResult over = Minecraft.getMinecraft().objectMouseOver;
		if(over != null && over.typeOfHit == Type.BLOCK && over.getBlockPos().equals(te.getPos()))
		{
			try
			{
				Field f = PlayerControllerMP.class.getDeclaredFields()[4];
				f.setAccessible(true);
				float progress = f.getFloat(Minecraft.getMinecraft().playerController);
				if(progress > 0F) destroy = DestroyStage.getByProgress(progress);
			}catch(Throwable err) {}
		}
		
		Cuboid6 cbd = ((BlockMultipart) ModBlocks.MULTIPART).getCuboidFromRTR(te.getWorld(), over);
		AxisAlignedBB aabb = cbd != null ? cbd.aabb() : null;
		
		ResourceLocation _destroy = destroy;
		
		/* Move to stream.forEach(Lambda) function as it is almost twice (or more) the speed */
		mps.forEach(s ->
		{
			IMultipartRender render = MultipartRenderingRegistry.getRender(s);
			GL11.glPushMatrix();
			if(render != null) render.renderMultipartAt(s, x, y, z, partialTicks, aabb != null && s.getBoundingBox() != null && aabb.equals(s.getBoundingBox()) ? _destroy : null);
			GL11.glPopMatrix();
		});
		
//		drawWithoutReset(te.VertexBuffer);
	}
	
	private void redraw()
	{
		
	}
	
	@Override
	public void renderTileEntityFast(TileMultipart te, double x, double y, double z, float partialTicks, int destroyStage, VertexBuffer buffer)
	{
		Stream<MultipartSignature> mps = te.signatures().stream();
		if(mps == null) return;
		
		mps.forEach(s ->
		{
			IMultipartRender render = MultipartRenderingRegistry.getRender(s);
			GL11.glPushMatrix();
			if(render != null) render.renderMultipartAt(s, x, y, z, partialTicks, null);
			GL11.glPopMatrix();
		});
	}
	
	public static class DestroyStage
	{
		private static final ResourceLocation[] DESTROY_STAGES = new ResourceLocation[]
		{
			new ResourceLocation("hammercore", "textures/models/destroy_stage_0.png"),
			new ResourceLocation("hammercore", "textures/models/destroy_stage_1.png"),
			new ResourceLocation("hammercore", "textures/models/destroy_stage_2.png"),
			new ResourceLocation("hammercore", "textures/models/destroy_stage_3.png"),
			new ResourceLocation("hammercore", "textures/models/destroy_stage_4.png"),
			new ResourceLocation("hammercore", "textures/models/destroy_stage_5.png"),
			new ResourceLocation("hammercore", "textures/models/destroy_stage_6.png"),
			new ResourceLocation("hammercore", "textures/models/destroy_stage_7.png"),
			new ResourceLocation("hammercore", "textures/models/destroy_stage_8.png"),
			new ResourceLocation("hammercore", "textures/models/destroy_stage_9.png")
		};
		
		public static ResourceLocation getByProgress(float progress)
		{
			return DESTROY_STAGES[(int) Math.round(progress * (DESTROY_STAGES.length - 1))];
		}
	}
	
	public void drawWithoutReset(VertexBuffer vertexBufferIn)
    {
        if (vertexBufferIn.getVertexCount() > 0)
        {
            VertexFormat vertexformat = vertexBufferIn.getVertexFormat();
            int i = vertexformat.getNextOffset();
            ByteBuffer bytebuffer = vertexBufferIn.getByteBuffer();
            List<VertexFormatElement> list = vertexformat.getElements();

            for (int j = 0; j < list.size(); ++j)
            {
                VertexFormatElement vertexformatelement = (VertexFormatElement)list.get(j);
                VertexFormatElement.EnumUsage vertexformatelement$enumusage = vertexformatelement.getUsage();
                int k = vertexformatelement.getType().getGlConstant();
                int l = vertexformatelement.getIndex();
                bytebuffer.position(vertexformat.getOffset(j));

                // moved to VertexFormatElement.preDraw
                vertexformatelement.getUsage().preDraw(vertexformat, j, i, bytebuffer);
            }

            GlStateManager.glDrawArrays(vertexBufferIn.getDrawMode(), 0, vertexBufferIn.getVertexCount());
            int i1 = 0;

            for (int j1 = list.size(); i1 < j1; ++i1)
            {
                VertexFormatElement vertexformatelement1 = (VertexFormatElement)list.get(i1);
                VertexFormatElement.EnumUsage vertexformatelement$enumusage1 = vertexformatelement1.getUsage();
                int k1 = vertexformatelement1.getIndex();

                // moved to VertexFormatElement.postDraw
                vertexformatelement1.getUsage().postDraw(vertexformat, i1, i, bytebuffer);
            }
        }
    }
}