package com.mrdimka.hammercore.client.renderer;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import com.mrdimka.hammercore.client.renderer.item.EnumItemRender;
import com.mrdimka.hammercore.client.renderer.item.IItemRenderer;

public interface IRenderHelper
{
	ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");
	
	default void renderEndPortalEffect(double x, double y, double z, EnumFacing... renderSides)
	{
		renderEndPortalEffect(x, y, z, END_PORTAL_TEXTURE, renderSides);
	}
	
	void renderEndPortalEffect(double x, double y, double z, ResourceLocation end_portal_texture, EnumFacing... renderSides);
	void registerItemRender(Item item, IItemRenderer renderer);
	IItemRenderer getRenderFor(ItemStack stack, EnumItemRender type);
	IItemRenderer getRenderFor(Item item);
}