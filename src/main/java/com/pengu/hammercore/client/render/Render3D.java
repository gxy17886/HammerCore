package com.pengu.hammercore.client.render;

import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerX;
import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerY;
import static net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher.staticPlayerZ;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.vec.Cuboid6;
import com.pengu.hammercore.client.render.vertex.ComplicatedRendering;

public class Render3D
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void renderWorld(RenderWorldLastEvent evt)
	{
		
	}
	
	@SubscribeEvent
	public void renderBlock(DrawBlockHighlightEvent evt)
	{
		try
		{
			RayTraceResult rtl = evt.getTarget();
			if(rtl == null || rtl.getBlockPos() == null || evt.getPlayer() == null || evt.getPlayer().world == null)
				return;
			
			BlockPos pos = rtl.getBlockPos();
			TileEntity tile = evt.getPlayer().world.getTileEntity(pos);
			IBlockState state = evt.getPlayer().world.getBlockState(pos);
			
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glTranslated(pos.getX() - staticPlayerX, pos.getY() - staticPlayerY, pos.getZ() - staticPlayerZ);
			
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);
			
			double bp = 6 / 16D;
			double ep = 10 / 16D;
			
			ComplicatedRendering.renderLines(0, 0, 0, ComplicatedRendering.forRendering(ComplicatedRendering.forRendering( new Cuboid6(0, 0, 0, 1, 1, 1), new Cuboid6(0, 1, 0, 1, 2, 1) )));
			
//			evt.setCanceled(true);
			
			GL11.glPopMatrix();
		} catch(Throwable err)
		{
		} // Got problems? Nah, ignore it!	
	}
}