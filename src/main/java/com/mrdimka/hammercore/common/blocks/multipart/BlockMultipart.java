package com.mrdimka.hammercore.common.blocks.multipart;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.mrdimka.hammercore.api.INoItemBlock;
import com.mrdimka.hammercore.api.mhb.BlockTraceable;
import com.mrdimka.hammercore.api.mhb.ICubeManager;
import com.mrdimka.hammercore.api.multipart.ItemBlockMultipartProvider;
import com.mrdimka.hammercore.api.multipart.MultipartSignature;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.vec.Cuboid6;

public class BlockMultipart extends BlockTraceable implements ITileEntityProvider, ICubeManager, INoItemBlock
{
	private static final Cuboid6[] EMPTY_CUBOID_ARRAY = new Cuboid6[0];
	
	public BlockMultipart()
	{
		super(Material.IRON);
		MinecraftForge.EVENT_BUS.register(this);
		setUnlocalizedName("multipart");
	}
	
	@Override
	public AxisAlignedBB getFullBoundingBox(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return FULL_BLOCK_AABB;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.INVISIBLE;
	}
	
	@Override
	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity)
	{
		if(entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;
			
			Cuboid6 cbd = getCuboidFromPlayer(player, pos);
			TileMultipart tmp = WorldUtil.cast(world.getTileEntity(pos), TileMultipart.class);
			if(tmp != null && cbd != null)
			{
				MultipartSignature s = tmp.getSignature(cbd.center().toVec3d());
				if(s.getState() != null) return s.getState().getBlock().getSoundType(s.getState(), world, pos, player);
			}
		}
		return super.getSoundType(state, world, pos, entity);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState p_isOpaqueCube_1_)
	{
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState p_isOpaqueCube_1_)
	{
		return false;
	}
	
	@Override
	public boolean isFullyOpaque(IBlockState p_isFullyOpaque_1_)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState p_isFullCube_1_)
	{
		return false;
	}
	
	@Override
	public boolean causesSuffocation(IBlockState state)
	{
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileMultipart();
	}
	
	@Override
	public Cuboid6[] getCuboids(World world, BlockPos pos, IBlockState state)
	{
		TileMultipart tmp = WorldUtil.cast(world.getTileEntity(pos), TileMultipart.class);
		return tmp != null ? tmp.getCuboids() : EMPTY_CUBOID_ARRAY;
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		TileMultipart tmp = WorldUtil.cast(world.getTileEntity(pos), TileMultipart.class);
		return tmp != null ? tmp.getLightLevel() : 0;
	}
	
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return false;
	}
	
	@Override
	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
	{
		return true;
	}
	
	@Override
	public boolean onBoxActivated(int boxID, Cuboid6 box, World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		Cuboid6 cbd = getCuboidFromPlayer(playerIn, pos);
		TileMultipart tmp = WorldUtil.cast(worldIn.getTileEntity(pos), TileMultipart.class);
		boolean activated = tmp != null ? tmp.onBoxActivated(boxID, cbd, worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ) : false;
		if(!activated)
		{
			ItemStack stack = playerIn.getHeldItem(hand);
			if(stack.getItem() instanceof ItemBlockMultipartProvider)
			{
				EnumActionResult r = stack.getItem().onItemUse(playerIn, worldIn, pos, hand, facing, hitX + 1, hitY, hitZ);
				if(r == EnumActionResult.SUCCESS) playerIn.swingArm(hand);
			}
		}
		return activated;
	}
	
	@Override
	public float getPlayerRelativeBlockHardness(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos)
	{
		Cuboid6 cbd = getCuboidFromPlayer(player, pos);
		TileMultipart tmp = WorldUtil.cast(worldIn.getTileEntity(pos), TileMultipart.class);
		
		if(tmp != null && cbd != null)
		{
			MultipartSignature signature = tmp.getSignature(cbd.center().toVec3d());
			return signature.getState() != null ? signature.getState().getPlayerRelativeBlockHardness(player, worldIn, pos) : 0F;
		}
		return 0F;
	}
	
	@SubscribeEvent
	public void tryBreakBlock(BlockEvent.BreakEvent evt)
	{
		if(evt.getState().getBlock() == this)
		{
			TileMultipart tmp = WorldUtil.cast(evt.getWorld().getTileEntity(evt.getPos()), TileMultipart.class);
			
			if(tmp != null && !evt.getWorld().isRemote)
			{
				Cuboid6 cbd = getCuboidFromPlayer(evt.getPlayer(), evt.getPos());
				if(cbd != null)
				{
					MultipartSignature signature = tmp.getSignature(cbd.center().toVec3d());
					if(signature != null)
					{
						tmp.removeMultipart(signature, !evt.getPlayer().capabilities.isCreativeMode);
						evt.setCanceled(true);
					}
				}
			}
		}
	}
}