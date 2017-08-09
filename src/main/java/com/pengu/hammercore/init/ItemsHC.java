package com.pengu.hammercore.init;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.mrdimka.hammercore.common.items.ItemBattery;
import com.mrdimka.hammercore.common.items.ItemCalculatron;
import com.mrdimka.hammercore.common.items.ItemWrench;
import com.mrdimka.hammercore.common.items.MultiVariantItem;
import com.mrdimka.hammercore.common.items.debug.ItemRayTracer;
import com.mrdimka.hammercore.common.items.debug.ItemZapper;
import com.pengu.hammercore.cfg.HammerCoreConfigs;

public class ItemsHC
{
	public static final transient Set<Item> items = new HashSet<Item>();
	public static final transient Set<MultiVariantItem> multiitems = new HashSet<MultiVariantItem>();
	
	public static final Item //
	        ray_tracer, //
	        zapper, //
	        calculatron = new ItemCalculatron(), //
	        WRENCH = new ItemWrench(), //
	        BATTERY = new ItemBattery(2_000_000, 25_000).setUnlocalizedName("battery"),
	        IRON_GEAR = new Item().setMaxStackSize(16).setUnlocalizedName("iron_gear");
	
	static
	{
		if(HammerCoreConfigs.debug_addRaytracer)
			ray_tracer = new ItemRayTracer();
		else
			ray_tracer = null;
		
		if(HammerCoreConfigs.debug_addZapper)
			zapper = new ItemZapper();
		else
			zapper = null;
	}
	
	public static void registerItem(Item i, String modid, CreativeTabs tab)
	{
		SimpleRegistration.registerItem(i, modid, tab);
	}
}