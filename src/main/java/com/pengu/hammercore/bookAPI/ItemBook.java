package com.pengu.hammercore.bookAPI;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import com.pengu.hammercore.HammerCore;

public abstract class ItemBook extends Item
{
	public abstract String getBookId();
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		if(worldIn.isRemote)
			HammerCore.bookProxy.openBookGui(getBookId());
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}