package com.mrdimka.hammercore.client.renderer;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import com.mrdimka.hammercore.client.renderer.item.EnumItemRender;
import com.mrdimka.hammercore.client.renderer.item.IItemRenderer;

public interface IRenderHelper
{
	public void renderEndPortalEffect(double x, double y, double z, EnumFacing... renderSides);
	public void registerItemRender(Item item, IItemRenderer renderer);
	public IItemRenderer getRenderFor(ItemStack stack, EnumItemRender type);
	public IItemRenderer getRenderFor(Item item);
}