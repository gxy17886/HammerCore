package com.pengu.hammercore.common.inventory;

import net.minecraft.item.ItemStack;

public interface ISlotPredicate
{
	boolean test(int slot, ItemStack stack);
}