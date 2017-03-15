package com.mrdimka.hammercore.client.model;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @deprecated Move to {@link HCModelRegistry}
 */
@Deprecated
@SideOnly(Side.CLIENT)
public class SimpleModelRegistry
{
	/**
	 * @deprecated Move to {@link HCModelRegistry#INSTANCE}
	 */
	@Deprecated
	public static final HCModelRegistry INSTANCE = HCModelRegistry.INSTANCE;
}