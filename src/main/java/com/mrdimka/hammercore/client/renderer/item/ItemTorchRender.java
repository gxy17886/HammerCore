package com.mrdimka.hammercore.client.renderer.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.client.UV;
import com.mrdimka.hammercore.client.renderer.RenderSmoke;

public class ItemTorchRender implements IItemRenderer
{
	public static final ResourceLocation particlel = new ResourceLocation("hammercore", "textures/gui/particles.png");
	public static final UV[] SMOKE_UVS = { new UV(particlel, 56, 0, 8, 8), new UV(particlel, 48, 0, 8, 8), new UV(particlel, 40, 0, 8, 8), new UV(particlel, 32, 0, 8, 8), new UV(particlel, 24, 0, 8, 8), new UV(particlel, 16, 0, 8, 8), new UV(particlel, 8, 0, 8, 8), new UV(particlel, 0, 0, 8, 8) };
	
	public List<RenderSmoke> effects = new ArrayList<RenderSmoke>();
	private long lastAdd = 0;
	
	public static final Random rand = new Random();
	
	@Override
	public boolean handleRenderFor(EnumItemRender render, ItemStack stack)
	{
		return render == EnumItemRender.GUI && stack != null && (stack.getItem() == Item.getItemFromBlock(Blocks.TORCH) || stack.getItem() == Item.getItemFromBlock(Blocks.REDSTONE_TORCH));
	}
	
	@Override
	public void render(EnumItemRender render, ItemStack stack, double x, double y, double z)
	{
		long currentTimeMS = System.currentTimeMillis() + (long) hashCode();
		rand.setSeed(currentTimeMS);
		if(rand.nextInt(100) <= 2 && currentTimeMS - lastAdd >= 50)
		{
			lastAdd = currentTimeMS;
			effects.add(new RenderSmoke(750L));
		}
		
		boolean useColored = false;
		for(RenderSmoke r : effects)
		{
			rand.setSeed(hashCode() + r.hashCode() + stack.hashCode());
			if(useColored) GL11.glColor3d(rand.nextDouble(), rand.nextDouble(), rand.nextDouble());
			else
			{
				if(stack.getItem() == Item.getItemFromBlock(Blocks.TORCH))
				{
					double rgb = rand.nextDouble() / 3D;
					GL11.glColor3d(rgb, rgb, rgb);
				}
				else if(stack.getItem() == Item.getItemFromBlock(Blocks.REDSTONE_TORCH)) GL11.glColor3d(.25 + rand.nextDouble() / 1.25D, .1, .1);
			}
			GL11.glPushMatrix();
			GL11.glTranslated(x + 1 + rand.nextDouble() * 6, y + rand.nextDouble() * 4 - 1, z);
			r.render();
			GL11.glPopMatrix();
			if(!r.shouldRender()) effects.set(effects.indexOf(r), null);
			GL11.glColor3d(1, 1, 1);
		}
		
		while(effects.contains(null)) effects.remove(null);
	}
}