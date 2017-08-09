package com.mrdimka.hammercore.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import com.mrdimka.hammercore.common.IWrenchItem;

public class ItemWrench extends Item implements IWrenchItem
{
	public ItemWrench()
	{
		setUnlocalizedName("wrench");
		setMaxStackSize(1);
	}
	
	@Override
	public boolean canWrench(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public void onWrenchUsed(EntityPlayer player, BlockPos pos, EnumHand hand)
	{
	}
}