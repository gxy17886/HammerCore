package com.pengu.hammercore.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.pengu.hammercore.api.ITileBlock;
import com.pengu.hammercore.tile.TileInfiRF;

public class BlockInfiRF extends Block implements ITileEntityProvider, ITileBlock<TileInfiRF>
{
	public BlockInfiRF()
	{
		super(Material.IRON);
		setUnlocalizedName("infi_rf");
		setHardness(-1);
		setResistance(Float.MAX_VALUE);
	}
	
	@Override
	public Class<TileInfiRF> getTileClass()
	{
		return TileInfiRF.class;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileInfiRF();
	}
}