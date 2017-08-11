package com.mrdimka.hammercore.client.renderer;

import net.minecraft.util.EnumFacing;

public interface IRenderHelper
{
	public void renderEndPortalEffect(double x, double y, double z, EnumFacing... renderSides);
}