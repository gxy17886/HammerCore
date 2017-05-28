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
	
	@ModConfigPropertyBool(name = "Enable", category = "Snowfall", defaultValue = true, comment = "Should Hammer Core use Snowfall feature?")
	public static boolean snowfall_enabled;
	
	@ModConfigPropertyBool(name = "Snow World", category = "Snowfall", defaultValue = false, comment = "Make whole world cover with snow! Yay for penguins!")
	public static boolean snowfall_snowWorld;
	
	@ModConfigPropertyBool(name = "Snow Melting", category = "Snowfall", defaultValue = false, comment = "Should snow layers melt depending on biome?")
	public static boolean snowfall_meltSnow;
	
	@ModConfigPropertyBool(name = "Snow Balancing", category = "Snowfall", defaultValue = true, comment = "Should snow layers balance out? \nWarning: This also allows snow to fall from mountains!")
	public static boolean snowfall_balanceSnow;
	
	public static Configuration cfg;
	
	@Override
	public void reloadCustom(Configuration cfgs)
	{
		cfg = cfgs;
	}
}