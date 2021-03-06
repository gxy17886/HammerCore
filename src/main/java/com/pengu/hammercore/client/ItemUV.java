package com.pengu.hammercore.client;

import static com.pengu.hammercore.client.GLRenderState.BLEND;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ItemUV extends UV
{
	public final ItemStack icon;
	
	public ItemUV(ItemStack icon)
	{
		super(TextureMap.LOCATION_BLOCKS_TEXTURE, 0, 0, 16, 16);
		this.icon = icon;
	}
	
	public void render(double x, double y)
	{
		BLEND.captureState();
		BLEND.on();
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, 0);
		Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(icon, 0, 0);
		GL11.glPopMatrix();
		BLEND.reset();
	}
	
	@Override
	public String toString()
	{
		return getClass().getName() + "@[item=" + icon + ",width=" + width + ",height=" + height + "]";
	}
}