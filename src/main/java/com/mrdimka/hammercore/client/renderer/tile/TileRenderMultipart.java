package com.mrdimka.hammercore.client.renderer.tile;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
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
import com.pengu.hammercore.client.DestroyStageTexture;
import com.pengu.hammercore.client.render.tesr.TESR;

public class TileRenderMultipart extends TESR<TileMultipart>
{
	@Override
	public void renderTileEntityAt(TileMultipart te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage)
	{
		try
		{
			List<MultipartSignature> mps = te.signatures();
			if(mps == null) return;
			
			BlockMultipart bmp = (BlockMultipart) ModBlocks.MULTIPART;
			World w = te.getWorld();
			EntityPlayer p = Minecraft.getMinecraft().player;
			Cuboid6 cbd = bmp.getCuboidFromRTR(te.getWorld(), bmp.collisionRayTrace(w.getBlockState(te.getPos()), w, te.getPos(), RayTracer.getCorrectedHeadVec(p), RayTracer.getEndVec(p)));
			AxisAlignedBB aabb = cbd != null ? cbd.aabb() : null;
			
			/* Moved to good ol' for loops; The fastest way :D */
			for(MultipartSignature s : mps)
			{
				IMultipartRender render = MultipartRenderingRegistry.getRender(s);
				GL11.glPushMatrix();
				if(render != null) render.renderMultipartAt(s, x, y, z, partialTicks, aabb != null && s.getBoundingBox() != null && aabb.equals(s.getBoundingBox()) ? destroyStage : null);
				GL11.glPopMatrix();
			}
		}catch(Throwable err) { HammerCore.LOG.error("Failed to render multipart at " + te.getPos() + ": " + err); } //we must ignore all issues that may arise!
	}
	
	/** False -- for future use */
	@Override
	public boolean canRenderFromNbt()
	{
	    return false;
	}
	
	/**
	 * @deprecated use {@link DestroyStageTexture} instead.
	 */
	@Deprecated
	public static class DestroyStage
	{
		/**
		 * @deprecated use {@link DestroyStageTexture#getByProgress(float)} instead.
		 */
		@Deprecated
		public static ResourceLocation getByProgress(float progress)
		{
			return DestroyStageTexture.getByProgress(progress);
		}
	}
}