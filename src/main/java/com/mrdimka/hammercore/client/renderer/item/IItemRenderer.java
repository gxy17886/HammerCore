package com.mrdimka.hammercore.client.renderer.item;

import net.minecraft.item.ItemStack;

public interface IItemRenderer
{
	public boolean handleRenderFor(EnumItemRender render, ItemStack stack);
	public void render(EnumItemRender render, ItemStack stack, double x, double y, double z);
}