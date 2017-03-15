package com.mrdimka.hammercore.api.multipart;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mrdimka.hammercore.common.blocks.multipart.TileMultipart;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.init.ModBlocks;

public class ItemBlockMultipartProvider extends ItemBlock
{
	public final BlockMultipartProvider provider;
	
	public ItemBlockMultipartProvider(BlockMultipartProvider block)
	{
		super(block);
		provider = block;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
        ItemStack itemstack = player.getHeldItem(hand);
        
        TileMultipart tmp = WorldUtil.cast(worldIn.getTileEntity(pos), TileMultipart.class);
        
        boolean canPlace = false;
        
        if((tmp == null && !block.isReplaceable(worldIn, pos)) || (tmp == null || !tmp.canPlace(provider.createSignature(tmp.getNextSignatureIndex(), itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ))) || player.isSneaking())
        {
        	if(hitX > 1F) return EnumActionResult.FAIL;
        	pos = pos.offset(facing);
        }
        
        canPlace = tmp != null && tmp.canPlace(provider.createSignature(tmp.getNextSignatureIndex(), itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ));
        if(!canPlace) canPlace = worldIn.mayPlace(this.block, pos, false, facing, null);
        
        if(!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack) && canPlace)
        {
            int i = this.getMetadata(itemstack.getMetadata());
            IBlockState iblockstate1 = this.block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player, hand);
            
            if(placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1))
            {
                SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, player);
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch()/* * 0.8F*/);
                if(!worldIn.isRemote) itemstack.shrink(1);
            }
            
            return EnumActionResult.SUCCESS;
        }
        else return EnumActionResult.FAIL;
    }
	
	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState)
	{
		TileMultipart tmp = WorldUtil.cast(world.getTileEntity(pos), TileMultipart.class);
		if(tmp == null) world.setBlockState(pos, ModBlocks.MULTIPART.getDefaultState(), 11);
		tmp = WorldUtil.cast(world.getTileEntity(pos), TileMultipart.class);
		if(tmp == null) world.setTileEntity(pos, tmp = new TileMultipart());
		
		MultipartSignature sign = provider.createSignature(tmp.getNextSignatureIndex(), stack, player, world, pos, side, hitX, hitY, hitZ);
		if(sign == null) return false;
		
		sign.setState(newState);
		boolean placed = tmp.addMultipart(sign);
		
		return placed;
	}
}