package com.pengu.hammercore.client.render.item;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum ItemRenderingHandler
{
	INSTANCE;
	
	private final Map<Item, iItemRender> renders = new HashMap<>();
	
	public void bindItemRender(Item item, iItemRender render)
	{
		renders.put(item, render);
		Minecraft.getMinecraft().getRenderItem().registerItem(item, 0, "chest");
	}
	
	public boolean canRender(Item item)
	{
		return getRender(item) != null;
	}
	
	public iItemRender getRender(Item item)
	{
		return renders.get(item);
	}
}