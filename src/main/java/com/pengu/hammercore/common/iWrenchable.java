package com.pengu.hammercore.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

import com.pengu.hammercore.utils.WorldLocation;

/**
 * Universal interface to be applied to both Block and TileEntity
 */
public interface iWrenchable
{
	/**
	 * Called when player clicks with {@link iWrenchItem} if
	 * {@link iWrenchItem#canWrench(net.minecraft.item.ItemStack)} returns true.
	 */
	public boolean onWrenchUsed(WorldLocation loc, EntityPlayer player, EnumHand hand);
}