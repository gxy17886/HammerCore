package com.pengu.hammercore.init;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.mrdimka.hammercore.common.items.ItemCalculatron;
import com.mrdimka.hammercore.common.items.MultiVariantItem;
import com.mrdimka.hammercore.common.items.debug.ItemRayTracer;
import com.mrdimka.hammercore.common.items.debug.ItemZapper;
import com.mrdimka.hammercore.config.HCConfigs;

public class ItemsHC
{
	public static final transient Set<Item> items = new HashSet<Item>();
	public static final transient Set<MultiVariantItem> multiitems = new HashSet<MultiVariantItem>();
	
	public static final Item //
	        ray_tracer, //
	        zapper, //
	        calculatron;
	
	static
	{
		if(HCConfigs.custom_addCalculatron)
			calculatron = new ItemCalculatron();
		else
			calculatron = null;
		
		if(HCConfigs.debug_addRayTracer)
			ray_tracer = new ItemRayTracer();
		else
			ray_tracer = null;
		
		if(HCConfigs.debug_addZapper)
			zapper = new ItemZapper();
		else
			zapper = null;
	}
	
	public static void registerItem(Item i, String modid, CreativeTabs tab)
	{
		SimpleRegistration.registerItem(i, modid, tab);
	}
}