package com.mrdimka.hammercore.cfg;

import net.minecraftforge.common.config.Configuration;

import com.mrdimka.hammercore.cfg.fields.ModConfigPropertyBool;

@HCModConfigurations(modid = "hammercore")
public class HammerCoreConfigs implements IConfigReloadListener
{
	@ModConfigPropertyBool(name = "Smooth Vanilla Guis", category = "Client", defaultValue = true, comment = "Replace vanilla furnace and brewing stand progress bars to use smooth rendering?")
	public static boolean client_smoothVanillaGuis = true;
	
	@ModConfigPropertyBool(name = "Mod Browser", category = "Client", defaultValue = true, comment = "Should Hammer Core add Mod Browser feature to main menu?")
	public static boolean client_modBrowser = true;
	
	@ModConfigPropertyBool(name = "Zapper", category = "Debug", defaultValue = false, comment = "Should Hammer Core add debug zapper item?")
	public static boolean debug_addZapper = false;
	
	@ModConfigPropertyBool(name = "Raytracer", category = "Debug", defaultValue = false, comment = "Should Hammer Core add debug ray tracer item?")
	public static boolean debug_addRaytracer = false;
	
	@ModConfigPropertyBool(name = "Always Spawn Dragon Egg", category = "Vanilla Improvements", defaultValue = true, comment = "Should Hammer Core force-spawn Ender Dragon Egg on Ender Dragon death?")
	public static boolean vanilla_alwaysSpawnDragonEggs = true;
	
	public static Configuration cfg;
	
	@Override
	public void reloadCustom(Configuration cfgs)
	{
		cfg = cfgs;
	}
}