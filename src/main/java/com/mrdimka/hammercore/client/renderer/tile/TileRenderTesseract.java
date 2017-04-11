package com.mrdimka.hammercore.client.renderer.tile;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.common.blocks.tesseract.BlockTesseract;
import com.mrdimka.hammercore.common.blocks.tesseract.TileTesseract;
import com.mrdimka.hammercore.init.ModBlocks;

public class TileRenderTesseract extends TileEntitySpecialRenderer<TileTesseract>
{
	@Override
	public void renderTileEntityAt(TileTesseract te, double x, double y, double z, float partialTicks, int destroyStage)
	{
		IBlockState state = te.getWorld().getBlockState(te.getPos());
		
		if(state.getBlock() == ModBlocks.TESSERACT && state.getValue(BlockTesseract.active))
		{
			GL11.glPushMatrix();
			GL11.glTranslated(x + .1, y + .1, z + .1);
			GL11.glScaled(.8, .8, .8);
			HammerCore.renderProxy.getRenderHelper().renderEndPortalEffect(0, 0, 0, EnumFacing.VALUES);
			GL11.glPopMatrix();
		}
	}
}