package com.pengu.hammercore.init;

import net.minecraft.block.Block;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.common.blocks.BlockInfiRF;
import com.pengu.hammercore.common.blocks.multipart.BlockMultipart;
import com.pengu.hammercore.common.blocks.tesseract.BlockTesseract;

public class ModBlocks
{
	public static final Block //
	        INFI_RF = new BlockInfiRF(), //
	        MULTIPART = new BlockMultipart(), //
	        TESSERACT = new BlockTesseract();
	
	@Deprecated
	public static void registerBlock(Block b, String modid)
	{
		SimpleRegistration.registerBlock(b, modid, HammerCore.tab);
	}
}