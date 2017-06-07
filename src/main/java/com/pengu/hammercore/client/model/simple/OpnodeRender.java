package com.pengu.hammercore.client.model.simple;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import scala.actors.threadpool.Arrays;

import com.pengu.hammercore.client.render.vertex.SimpleBlockRendering;

public class OpnodeRender
{
	public void renderOpnodes(SimpleBlockRendering sbr, List<Opnode> nodes, int bright, boolean newTessellation)
	{
		if(newTessellation)
			sbr.begin();
		for(Opnode node : nodes)
			renderOpnode(sbr, node, bright, false);
		if(newTessellation)
			sbr.end();
	}
	
	public void renderOpnode(SimpleBlockRendering sbr, Opnode node, int bright, boolean newTessellation)
	{
		if(newTessellation)
			sbr.begin();
		sbr.setBrightness(bright);
		int[] opnode = node.opnode;
		for(EnumFacing facing : EnumFacing.VALUES)
			sbr.setSpriteForSide(facing, Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(node.textures[facing.ordinal()]));
		for(int i = 0; i < opnode.length; ++i)
		{
			int code = opnode[i];
			
			if(code == ModelOpcodes.DFACE)
			{
				i++;
				EnumFacing f = EnumFacing.VALUES[opnode[i]];
				sbr.disableFace(f);
				continue;
			} else
			
			if(code == ModelOpcodes.EFACE)
			{
				i++;
				EnumFacing f = EnumFacing.VALUES[opnode[i]];
				sbr.enableFace(f);
				continue;
			} else
			
			if(code == ModelOpcodes.COLOR)
			{
				i++;
				int r = opnode[i];
				i++;
				int g = opnode[i];
				i++;
				int b = opnode[i];
				i++;
				int a = opnode[i];
				Arrays.fill(sbr.rgb, (r >> 16) | (g >> 8) | b);
				sbr.rb.renderAlpha = a;
				continue;
			} else
			
			if(code == ModelOpcodes.BOUNDS)
			{
				double m1 = SimpleModelParser.getDouble(opnode, i + 1);
				double m2 = SimpleModelParser.getDouble(opnode, i + 3);
				double m3 = SimpleModelParser.getDouble(opnode, i + 5);
				double m4 = SimpleModelParser.getDouble(opnode, i + 7);
				double m5 = SimpleModelParser.getDouble(opnode, i + 9);
				double m6 = SimpleModelParser.getDouble(opnode, i + 11);
				sbr.setRenderBounds(m1, m2, m3, m4, m5, m6);
				i += 12;
				continue;
			} else if(code == ModelOpcodes.EFACES)
				sbr.enableFaces();
			else if(code == ModelOpcodes.DFACES)
				sbr.disableFaces();
			else if(code == ModelOpcodes.DRAW)
				sbr.drawBlock(0, 0, 0);
		}
		if(newTessellation)
			sbr.end();
		Arrays.fill(sbr.rgb, 0xFFFFFF);
		sbr.rb.renderAlpha = 255;
	}
}