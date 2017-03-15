package com.mrdimka.hammercore.init;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.api.INoItemBlock;
import com.mrdimka.hammercore.api.multipart.BlockMultipartProvider;
import com.mrdimka.hammercore.common.blocks.BlockInfiRF;
import com.mrdimka.hammercore.common.blocks.multipart.BlockMultipart;

public class ModBlocks
{
	public static final Block
							INFI_RF = new BlockInfiRF(),
							MULTIPART = new BlockMultipart();
	
	static
	{
		Field[] fs = ModBlocks.class.getFields();
		for(Field f : fs)
		{
			if(Block.class.isAssignableFrom(f.getType()))
			{
				try
				{
					registerBlock((Block) f.get(null));
				}catch (Throwable err) {}
			}
		}
	}

	public static void registerBlock(Block b)
	{
		String name = b.getUnlocalizedName().substring("tile.".length());
		b.setUnlocalizedName("hammercore:" + name);
		b.setCreativeTab(HammerCore.tab);
		
		//ItemBlockDefinition
		ItemBlock ib = null;
		
		if(b instanceof BlockMultipartProvider) ib = ((BlockMultipartProvider) b).createItem();
		else ib = new ItemBlock(b);
		
		GameRegistry.register(b, new ResourceLocation("hammercore", name));
		if(!(b instanceof INoItemBlock)) GameRegistry.register(ib.setRegistryName(b.getRegistryName()));
		
		if(b instanceof ITileEntityProvider)
		{
			ITileEntityProvider te = (ITileEntityProvider) b;
			TileEntity t = te.createNewTileEntity(null, 0);
			if(t != null) GameRegistry.registerTileEntity(t.getClass(), t.getClass().getName());
		}
		
		if(!(b instanceof INoItemBlock))
		{
			Item i = Item.getItemFromBlock(b);
			if(i != null) ModItems.items.add(i);
		}
	}
}