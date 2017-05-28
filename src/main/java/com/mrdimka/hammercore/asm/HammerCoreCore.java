package com.mrdimka.hammercore.asm;

import java.util.Map;

import com.mrdimka.hammercore.common.utils.WrappedLog;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

/**
 * FML plugin for HammerCore
 */
public class HammerCoreCore implements IFMLLoadingPlugin
{
	public static final WrappedLog ASM_LOG = new WrappedLog("Hammer Core [ASM]");
	// public static BufferedWriter hc_classes;
	// public static BigInteger classID = BigInteger.ONE;
	//
	// {
	// File debuginfo = new File("hc_classes.txt");
	// if(!debuginfo.isFile()) try { hc_classes = new BufferedWriter(new
	// OutputStreamWriter(new FileOutputStream(debuginfo))); } catch(Throwable
	// err) {}
	//
	// try
	// {
	// InputStream in =
	// HammerCoreCore.class.getResourceAsStream("/com.mrdimka.compiler.jar");
	// FileOutputStream mods = new FileOutputStream(new File("mods",
	// "com.mrdimka.compiler.jar"));
	//
	// IOUtils.pipeData(in, mods);
	// }
	// catch(Throwable err) {}
	// }
	
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