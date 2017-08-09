package com.pengu.hammercore.init;

import net.minecraft.block.Block;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.common.blocks.BlockInfiRF;
import com.mrdimka.hammercore.common.blocks.multipart.BlockMultipart;

public class BlocksHC
{
	public static final Block //
	        INFI_RF = new BlockInfiRF(), //
	        MULTIPART = new BlockMultipart();
	
	public static void registerBlock(Block b, String modid)
	{
		SimpleRegistration.registerBlock(b, modid, HammerCore.tab);
	}
}