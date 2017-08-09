package com.pengu.hammercore.asm;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import com.mrdimka.hammercore.common.utils.WrappedLog;

/**
 * FML plugin for HammerCore
 */
public class HammerCoreCore implements IFMLLoadingPlugin
{
	public static final WrappedLog ASM_LOG = new WrappedLog("Hammer Core [ASM]");
	
	@Override
	public String[] getASMTransformerClass()
	{
		return new String[] { HammerCoreTransformer.class.getName() };
	}
	
	@Override
	public String getModContainerClass()
	{
		return null;
	}
	
	@Override
	public String getSetupClass()
	{
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data)
	{
	}
	
	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}