package com.pengu.hammercore.init;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.mrdimka.hammercore.api.INoItemBlock;
import com.mrdimka.hammercore.api.ITileBlock;
import com.mrdimka.hammercore.api.multipart.BlockMultipartProvider;
import com.mrdimka.hammercore.common.blocks.IItemBlock;
import com.mrdimka.hammercore.common.items.MultiVariantItem;
import com.pengu.hammercore.utils.IRegisterListener;
import com.pengu.hammercore.utils.SoundObject;

public class SimpleRegistration
{
	public static void registerFieldItemsFrom(Class<?> owner, String modid, CreativeTabs tab)
	{
		Field[] fs = owner.getDeclaredFields();
		for(Field f : fs)
			if(Item.class.isAssignableFrom(f.getType()))
				try
				{
					f.setAccessible(true);
					registerItem((Item) f.get(null), modid, tab);
				} catch(Throwable err)
				{
				}
	}
	
	public static void registerFieldBlocksFrom(Class<?> owner, String modid, CreativeTabs tab)
	{
		Field[] fs = owner.getDeclaredFields();
		for(Field f : fs)
			if(Block.class.isAssignableFrom(f.getType()))
				try
				{
					f.setAccessible(true);
					registerBlock((Block) f.get(null), modid, tab);
				} catch(Throwable err)
				{
				}
	}
	
	public static void registerFieldSoundsFrom(Class<?> owner)
	{
		Field[] fs = owner.getDeclaredFields();
		for(Field f : fs)
			if(SoundObject.class.isAssignableFrom(f.getType()))
				try
				{
					f.setAccessible(true);
					registerSound((SoundObject) f.get(null));
				} catch(Throwable err)
				{
				}
	}
	
	/**
	 * Registers {@link SoundObject} to registry and populates
	 * {@link SoundObject} with {@link SoundEvent}.
	 **/
	public static void registerSound(SoundObject sound)
	{
		GameRegistry.findRegistry(SoundEvent.class).register(sound.sound = new SoundEvent(sound.name).setRegistryName(sound.name));
	}
	
	public static void registerItem(Item item, String modid, CreativeTabs tab)
	{
		if(item == null)
			return;
		String name = item.getUnlocalizedName().substring("item.".length());
		item.setRegistryName(modid, name);
		item.setUnlocalizedName(modid + ":" + name);
		if(tab != null)
			item.setCreativeTab(tab);
		GameRegistry.findRegistry(Item.class).register(item);
		if(item instanceof IRegisterListener)
			((IRegisterListener) item).onRegistered();
		if(item instanceof MultiVariantItem)
			ItemsHC.multiitems.add((MultiVariantItem) item);
		else
			ItemsHC.items.add(item);
	}
	
	public static void registerBlock(Block block, String modid, CreativeTabs tab)
	{
		if(block == null)
			return;
		String name = block.getUnlocalizedName().substring("tile.".length());
		block.setUnlocalizedName(modid + ":" + name);
		block.setCreativeTab(tab);
		
		// ItemBlockDefinition
		Item ib = null;
		
		if(block instanceof BlockMultipartProvider)
			ib = ((BlockMultipartProvider) block).createItem();
		else if(block instanceof IItemBlock)
			ib = ((IItemBlock) block).getItemBlock();
		else
			ib = new ItemBlock(block);
		
		block.setRegistryName(modid, name);
		GameRegistry.findRegistry(Block.class).register(block);
		if(!(block instanceof INoItemBlock))
			GameRegistry.findRegistry(Item.class).register(ib.setRegistryName(block.getRegistryName()));
		
		if(block instanceof IRegisterListener)
			((IRegisterListener) block).onRegistered();
		
		if(block instanceof ITileBlock)
		{
			Class c = ((ITileBlock) block).getTileClass();
			GameRegistry.registerTileEntity(c, modid + ":" + c.getName().substring(c.getName().lastIndexOf(".") + 1).toLowerCase());
		} else if(block instanceof ITileEntityProvider)
		{
			ITileEntityProvider te = (ITileEntityProvider) block;
			TileEntity t = te.createNewTileEntity(null, 0);
			if(t != null)
			{
				Class c = t.getClass();
				GameRegistry.registerTileEntity(c, modid + ":" + c.getName().substring(c.getName().lastIndexOf(".") + 1).toLowerCase());
			}
		}
		
		if(!(block instanceof INoItemBlock))
		{
			if(ib instanceof IRegisterListener)
				((IRegisterListener) ib).onRegistered();
			if(ib instanceof MultiVariantItem)
				ItemsHC.multiitems.add((MultiVariantItem) ib);
			else if(ib != null)
				ItemsHC.items.add(ib);
		}
	}
}