package com.mrdimka.hammercore.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

import com.pengu.hammercore.utils.WorldLocation;

/**
 * Universal interface to be applied to both Block and TileEntity
 */
public interface IWrenchable
{
	/**
	 * Called when player clicks with {@link IWrenchItem} if
	 * {@link IWrenchItem#canWrench(net.minecraft.item.ItemStack)} returns true.
	 */
	public boolean onWrenchUsed(WorldLocation loc, EntityPlayer player, EnumHand hand);
}