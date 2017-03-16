package com.mrdimka.hammercore.api.multipart;

import com.mrdimka.hammercore.api.INoItemBlock;
import com.mrdimka.hammercore.common.blocks.multipart.TileMultipart;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Baseline block that supports multipart. Use {@link ModBlocks#registerBlock(Block, String)} to register it.
 * <br>Optional interfaces: {@link INoItemBlock} (why do you need that?)
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
	
	public ItemBlockMultipartProvider createItem()
	{
		return (ItemBlockMultipartProvider) new ItemBlockMultipartProvider(this).setUnlocalizedName(getUnlocalizedName());
	}
}