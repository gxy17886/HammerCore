package com.pengu.hammercore.net.utils;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.pengu.hammercore.common.inventory.IInventoryListener;
import com.pengu.hammercore.common.inventory.InventoryNonTile;

public class NetPropertyInventoryNonTile extends NetPropertyAbstract<InventoryNonTile> implements IInventoryListener
{
	public NetPropertyInventoryNonTile(IPropertyChangeHandler handler)
	{
		super(handler);
	}
	
	public NetPropertyInventoryNonTile(IPropertyChangeHandler handler, InventoryNonTile initialValue)
	{
		super(handler, initialValue);
		if(value != null)
			value.listener = this;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		value = new InventoryNonTile(0);
		value.readFromNBT(nbt);
		value.listener = this;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		value.writeToNBT(nbt);
		return nbt;
	}

	@Override
	public void set(InventoryNonTile val)
	{
	    super.set(val);
	    if(value != null)
			value.listener = this;
	}
	
	@Override
	public InventoryNonTile getInventory()
	{
	    return value;
	}
	
	@Override
	public void slotChange(int slot, ItemStack stack)
	{
		handler.notifyOfChange(this);
		if(syncOnChange)
			handler.sendChangesToNearby();
	}
}