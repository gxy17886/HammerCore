package com.mrdimka.hammercore.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.mrdimka.hammercore.annotations.MCFBus;

@MCFBus
public class HCConfigs
{
	private static Configuration cfg;
	
	public static boolean vanilla_useSmoothGui;
	public static boolean custom_addCalculatron;
	public static boolean debug_addZapper, debug_addRayTracer;
	
	public static void init(File cfgFile)
	{
		if(cfg == null)
			cfg = new Configuration(cfgFile, true);
		reloadProperties();
	}
	
	public static void reloadProperties()
	{
		vanilla_useSmoothGui = cfg.getBoolean("Use Smooth Gui", "Vanilla Tweaks", true, "Should Hammer Core replace vanilla furnace and brewing stand GUI's progress bars with smooth versions?");
		
		custom_addCalculatron = cfg.getBoolean("Add Calculatron", "Custom Content", true, "Should Hammer Core add Calculatron?");
		
		debug_addRayTracer = cfg.getBoolean("Add Ray Tracer", "Debug Content", false, "Should Hammer Core add Ray Tracer?");
		debug_addZapper = cfg.getBoolean("Add Zapper", "Debug Content", false, "Should Hammer Core add Zapper?");
		
		if(cfg.hasChanged())
			cfg.save();
	}
	
	@SubscribeEvent
	public void configChanged(ConfigChangedEvent evt)
	{
		if(evt.getModID().equals("hammercore"))
			reloadProperties();
	}
}