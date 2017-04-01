package com.mrdimka.hammercore.common;

import net.minecraft.item.ItemStack;

public class InterItemStack
{
	public static final ItemStack NULL_STACK = null;
	
	public static int getStackSize(ItemStack stack)
	{
		return stack.stackSize;
	}
	
	public static void setStackSize(ItemStack stack, int size)
	{
		stack.stackSize = size;
	}
	
	public static boolean isStackNull(ItemStack stack)
	{
		return stack == null || getStackSize(stack) <= 0 || stack == NULL_STACK;
	}
}