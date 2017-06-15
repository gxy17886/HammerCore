package com.pengu.hammercore.common.blocks.tesseract;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.hammercore.api.ITileBlock;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.gui.GuiManager;
import com.pengu.hammercore.tile.TileSyncable;

public class BlockTesseract extends Block implements ITileEntityProvider, ITileBlock<TileTesseract>
{
	public static final PropertyBool active = PropertyBool.create("active");
	
	public BlockTesseract()
	{
		super(Material.IRON);
		setSoundType(SoundType.METAL);
		setHardness(15);
		setResistance(2000);
		setHarvestLevel("pickaxe", 2);
		setUnlocalizedName("tesseract");
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		TileSyncable tile = WorldUtil.cast(worldIn.getTileEntity(pos), TileSyncable.class);
		GuiManager.openGui(playerIn, tile);
		return tile != null && tile.hasGui();
	}
	
	@Override
	public Class<TileTesseract> getTileClass()
	{
		return TileTesseract.class;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileTesseract();
	}
	
	@Override
	public boolean canEntitySpawn(IBlockState state, Entity entityIn)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(active) ? 1 : 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(active, meta == 0 ? false : true);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, active);
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
}