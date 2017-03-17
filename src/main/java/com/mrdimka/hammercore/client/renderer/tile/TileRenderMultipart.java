package com.mrdimka.hammercore.client.renderer.tile;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.api.multipart.IMultipartRender;
import com.mrdimka.hammercore.api.multipart.MultipartRenderingRegistry;
import com.mrdimka.hammercore.api.multipart.MultipartSignature;
import com.mrdimka.hammercore.common.blocks.multipart.TileMultipart;

public class TileRenderMultipart extends TileEntitySpecialRenderer<TileMultipart>
{
	@Override
	public void renderTileEntityAt(TileMultipart te, double x, double y, double z, float partialTicks, int destroyStage)
	{
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
		
		for(MultipartSignature s : te.signatures())
		{
			IMultipartRender render = MultipartRenderingRegistry.getRender(s);
			GL11.glPushMatrix();
			if(render != null) render.renderMultipartAt(s, x, y, z, partialTicks, destroy);
			GL11.glPopMatrix();
		}
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