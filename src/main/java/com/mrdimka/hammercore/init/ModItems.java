package com.mrdimka.hammercore.init;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.common.items.ItemCalculatron;
import com.mrdimka.hammercore.common.items.MultiVariantItem;
import com.mrdimka.hammercore.common.items.debug.ItemRayTracer;
import com.mrdimka.hammercore.common.items.debug.ItemZapper;

public class ModItems
{
	public static final transient Set<Item> items = new HashSet<Item>();
	public static final transient Set<MultiVariantItem> multiitems = new HashSet<MultiVariantItem>();
	
	public static final Item
							ray_tracer = new ItemRayTracer(),
							zapper = new ItemZapper(),
							calculatron = new ItemCalculatron();
	
	static
	{
		Field[] fs = ModItems.class.getFields();
		for(Field f : fs) if(Item.class.isAssignableFrom(f.getType())) try {registerItem((Item) f.get(null), "hammercore", HammerCore.tab);}catch(Throwable err) {}
	}
	
	public static void registerItem(Item i, String modid, CreativeTabs tab)
	{
		String name = i.getUnlocalizedName().substring("item.".length());
		i.setRegistryName(modid, name);
		i.setUnlocalizedName(modid + ":" + name);
		if(tab != null) i.setCreativeTab(tab);
		GameRegistry.register(i);
		items.add(i);
	}
}