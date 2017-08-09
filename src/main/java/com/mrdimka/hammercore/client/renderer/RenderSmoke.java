package com.mrdimka.hammercore.client.renderer;

import net.minecraft.client.Minecraft;

import com.mrdimka.hammercore.client.renderer.item.ItemTorchRender;
import com.mrdimka.hammercore.client.utils.RenderUtil;

public class RenderSmoke implements IRenderable
{
	private long ageSpawn;
	private long ageMax;
	
	public RenderSmoke(long ageMax)
	{
		this.ageMax = ageMax;
	}
	
	@Override
	public void render()
	{
		if(shouldRender())
		{
			double yAdd = ((System.currentTimeMillis() - ageSpawn) % ageMax) / (double) ageMax;
			long elapsed = System.currentTimeMillis() - ageSpawn;
			int stage = (int) (((double) ageMax - (double) elapsed) / ((double) ageMax) * 7D);
			Minecraft.getMinecraft().getTextureManager().bindTexture(ItemTorchRender.particlel);
			RenderUtil.drawTexturedModalRect(0, yAdd * -4D, stage * 8, 0, 8, 8);
		}
	}
	
	public boolean shouldRender()
	{
		if(ageSpawn == 0L)
			ageSpawn = System.currentTimeMillis();
		long elapsed = System.currentTimeMillis() - ageSpawn;
		return elapsed < ageMax;
	}
}