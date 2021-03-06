package com.pengu.hammercore.client.render.tile;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.api.multipart.IMultipartRender;
import com.pengu.hammercore.api.multipart.MultipartRenderingRegistry;
import com.pengu.hammercore.api.multipart.MultipartSignature;
import com.pengu.hammercore.client.DestroyStageTexture;
import com.pengu.hammercore.client.render.tesr.TESR;
import com.pengu.hammercore.common.blocks.multipart.BlockMultipart;
import com.pengu.hammercore.common.blocks.multipart.TileMultipart;
import com.pengu.hammercore.init.BlocksHC;
import com.pengu.hammercore.raytracer.RayTracer;
import com.pengu.hammercore.vec.Cuboid6;

public class TileRenderMultipart extends TESR<TileMultipart>
{
	@Override
	public void renderTileEntityAt(TileMultipart te, double x, double y, double z, float partialTicks, ResourceLocation destroyStage, float alpha)
	{
		try
		{
			List<MultipartSignature> mps = te.signatures();
			if(mps == null)
				return;
			
			BlockMultipart bmp = (BlockMultipart) BlocksHC.MULTIPART;
			World w = te.getWorld();
			EntityPlayer p = Minecraft.getMinecraft().player;
			Cuboid6 cbd = bmp.getCuboidFromRTR(te.getWorld(), bmp.collisionRayTrace(w.getBlockState(te.getPos()), w, te.getPos(), RayTracer.getCorrectedHeadVec(p), RayTracer.getEndVec(p)));
			AxisAlignedBB aabb = cbd != null ? cbd.aabb() : null;
			
			/* Moved to good ol' for loops; The fastest way :D */
			for(MultipartSignature s : mps)
			{
				IMultipartRender render = MultipartRenderingRegistry.getRender(s);
				GL11.glPushMatrix();
				if(render != null)
					render.renderMultipartAt(s, x, y, z, partialTicks, aabb != null && s.getBoundingBox() != null && aabb.equals(s.getBoundingBox()) ? destroyStage : null);
				GL11.glPopMatrix();
			}
		} catch(Throwable err)
		{
			HammerCore.LOG.error("Failed to render multipart at " + te.getPos() + ": " + err);
		} // we must ignore all issues that may arise!
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
		 * @deprecated use {@link DestroyStageTexture#getByProgress(float)}
		 *             instead.
		 */
		@Deprecated
		public static ResourceLocation getByProgress(float progress)
		{
			return DestroyStageTexture.getByProgress(progress);
		}
	}
}