package com.mrdimka.hammercore.init;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.mrdimka.hammercore.cfg.HammerCoreConfigs;
import com.mrdimka.hammercore.common.items.ItemCalculatron;
import com.mrdimka.hammercore.common.items.ItemHammerCoreManual;
import com.mrdimka.hammercore.common.items.MultiVariantItem;
import com.mrdimka.hammercore.common.items.debug.ItemRayTracer;
import com.mrdimka.hammercore.common.items.debug.ItemZapper;
import com.pengu.hammercore.common.items.ItemBattery;

public class ModItems
{
	public static final transient Set<Item> items = new HashSet<Item>();
	public static final transient Set<MultiVariantItem> multiitems = new HashSet<MultiVariantItem>();
	
	public static final Item ray_tracer, zapper, calculatron = new ItemCalculatron(), manual = new ItemHammerCoreManual(), battery = new ItemBattery(2_000_000, 25_000).setUnlocalizedName("battery");
	
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
	
	@Deprecated
	public static void registerItem(Item i, String modid, CreativeTabs tab)
	{
		SimpleRegistration.registerItem(i, modid, tab);
	}
}