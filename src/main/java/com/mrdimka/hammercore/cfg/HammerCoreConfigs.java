package com.mrdimka.hammercore.cfg;

import java.io.File;

import com.mrdimka.hammercore.annotations.MCFBus;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@MCFBus
public class HammerCoreConfigs
{
	public static Configuration cfg;
	
	public static boolean client_smoothVanillaGuis = true;
	public static boolean debug_addZapper, debug_addRaytracer;
	
	public static void init(File cfgFile)
	{
		if(cfg != null) return;
		cfg = new Configuration(cfgFile, "@VERSION@");
	}
	
	public static void reload()
	{
		if(cfg == null) return;
		
		client_smoothVanillaGuis = cfg.getBoolean("Smooth Vanilla Guis", "Client", true, "Replace vanilla furnace and brewing stand progress bars to use smooth rendering?");
		
		debug_addZapper = cfg.getBoolean("Zapper", "Debug", false, "Should Hammer Core add debug zapper item?");
		debug_addRaytracer = cfg.getBoolean("Raytracer", "Debug", false, "Should Hammer Core add debug ray tracer item?");
		
		if(cfg.hasChanged()) cfg.save();
	}
	
	@SubscribeEvent
	public void cfgReloaded(ConfigChangedEvent evt)
	{
		if(evt.getModID().equals("hammercore")) reload();
	}
}