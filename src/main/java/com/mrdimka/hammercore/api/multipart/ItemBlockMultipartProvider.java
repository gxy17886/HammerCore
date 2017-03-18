package com.mrdimka.hammercore.api.multipart;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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

/**
 * ItemBlock for {@link BlockMultipartProvider}.
 */
public class ItemBlockMultipartProvider extends Item
{
	public final IMultipartProvider provider;
	
	public ItemBlockMultipartProvider(IMultipartProvider provider)
	{
		this.provider = provider;
	}
	
	/**
	 * Parameter-less version that casts this item into a provider
	 */
	public ItemBlockMultipartProvider()
	{
		this.provider = (IMultipartProvider) this;
	}
	
	@Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
		pos = pos.offset(facing);
		if(!worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)) return EnumActionResult.FAIL;
        TileMultipart tmp = MultipartAPI.getOrPlaceMultipart(worldIn, pos);
        
        ItemStack itemstack = player.getHeldItem(hand);
        if(tmp != null)
        {
            MultipartSignature s = provider.createSignature(tmp.getNextSignatureIndex(), itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ);
            
            if(tmp.canPlace(s) && tmp.addMultipart(s))
            {
                SoundType soundtype = s.getSoundType(player);
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch()/* * 0.8F*/);
                if(!worldIn.isRemote && !player.capabilities.isCreativeMode) itemstack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.FAIL;
    }
}