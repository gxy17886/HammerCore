package com.pengu.hammercore.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class ItemIWrench extends ItemWrench
{
	public ItemIWrench()
	{
		setUnlocalizedName("iwrench");
	}
	
	@Override
	public void onWrenchUsed(EntityPlayer player, BlockPos pos, EnumHand hand)
	{
	}
}