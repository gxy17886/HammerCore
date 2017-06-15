package com.pengu.hammercore.common.inventory;

import net.minecraft.item.ItemStack;

public interface IInventoryListener
{
	public void slotChange(int slot, ItemStack stack);
	
	public InventoryNonTile getInventory();
}