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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.api.multipart.IMultipartRender;
import com.mrdimka.hammercore.api.multipart.MultipartRenderingRegistry;
import com.mrdimka.hammercore.api.multipart.MultipartSignature;
import com.mrdimka.hammercore.common.blocks.multipart.BlockMultipart;
import com.mrdimka.hammercore.common.blocks.multipart.TileMultipart;
import com.mrdimka.hammercore.init.ModBlocks;
import com.mrdimka.hammercore.raytracer.RayTracer;
import com.mrdimka.hammercore.vec.Cuboid6;

public class TileRenderMultipart extends TileEntitySpecialRenderer<TileMultipart>
{
	@Override
	public void renderTileEntityAt(TileMultipart te, double x, double y, double z, float partialTicks, int destroyStage)
	{
		try
		{
			List<MultipartSignature> mps = te.signatures();
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
			
			BlockMultipart bmp = (BlockMultipart) ModBlocks.MULTIPART;
			World w = te.getWorld();
			EntityPlayer p = Minecraft.getMinecraft().player;
			Cuboid6 cbd = bmp.getCuboidFromRTR(te.getWorld(), bmp.collisionRayTrace(w.getBlockState(te.getPos()), w, te.getPos(), RayTracer.getCorrectedHeadVec(p), RayTracer.getEndVec(p)));
			AxisAlignedBB aabb = cbd != null ? cbd.aabb() : null;
			
			ResourceLocation _destroy = destroy;
			
			/* Moved to good ol' for loops; The fastest way :D */
			for(MultipartSignature s : mps)
			{
				IMultipartRender render = MultipartRenderingRegistry.getRender(s);
				GL11.glPushMatrix();
				if(render != null) render.renderMultipartAt(s, x, y, z, partialTicks, aabb != null && s.getBoundingBox() != null && aabb.equals(s.getBoundingBox()) ? _destroy : null);
				GL11.glPopMatrix();
			}
		}catch(Throwable err) { HammerCore.LOG.error("Failed to render multipart at " + te.getPos() + ": " + err); } //we must ignore all issues that may arise!
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
}