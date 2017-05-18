package com.mrdimka.hammercore.api.multipart;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

import com.mrdimka.hammercore.api.INoItemBlock;
import com.mrdimka.hammercore.init.ModBlocks;

/**
 * Baseline block that supports multipart. Use
 * {@link ModBlocks#registerBlock(Block, String)} to register it. <br>
 * Optional interfaces: {@link INoItemBlock} (why do you need that?)
 */
public abstract class BlockMultipartProvider extends Block implements IMultipartProvider
{
	public BlockMultipartProvider(Material materialIn)
	{
		super(materialIn);
	}
	
	public BlockMultipartProvider(Material blockMaterialIn, MapColor blockMapColorIn)
	{
		super(blockMaterialIn, blockMapColorIn);
	}
	
	public Item createItem()
	{
		return new ItemBlockMultipartProvider(this).setUnlocalizedName(getUnlocalizedName());
	}
}