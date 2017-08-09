package com.mrdimka.hammercore.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import com.mrdimka.hammercore.common.InterItemStack;
import com.mrdimka.hammercore.common.utils.WorldUtil;

/**
 * This is a part of Hammer Core InventoryNonTile is used widely to make
 * inventory code much more simple
 * 
 * @author MrDimka's Studio (MrDimka)
 */
public class InventoryNonTile implements IInventory
{
	public IInventoryListener listener;
	public ItemStack[] inventory = new ItemStack[27];
	private final int[] allSlots;
	public int inventoryStackLimit = 64;
	public NBTTagCompound boundCompound = new NBTTagCompound();
	
	public InventoryNonTile(int inventorySize, NBTTagCompound boundNBT)
	{
		inventory = new ItemStack[inventorySize];
		allSlots = new int[inventory.length];
		for(int i = 0; i < allSlots.length; ++i)
			allSlots[i] = i;
		boundCompound = boundNBT;
	}
	
	public InventoryNonTile(NBTTagCompound boundNBT, ItemStack... items)
	{
		inventory = new ItemStack[items.length];
		for(int i = 0; i < items.length; ++i)
			inventory[i] = items[i];
		allSlots = new int[items.length];
		for(int i = 0; i < allSlots.length; ++i)
			allSlots[i] = i;
		boundCompound = boundNBT;
	}
	
	public InventoryNonTile(int inventorySize)
	{
		inventory = new ItemStack[inventorySize];
		allSlots = new int[inventorySize];
		for(int i = 0; i < allSlots.length; ++i)
			allSlots[i] = i;
	}
	
	public InventoryNonTile(ItemStack... items)
	{
		inventory = new ItemStack[items.length];
		for(int i = 0; i < items.length; ++i)
			inventory[i] = items[i];
		allSlots = new int[items.length];
		for(int i = 0; i < allSlots.length; ++i)
			allSlots[i] = i;
	}
	
	public int[] getAllAvaliableSlots()
	{
		return allSlots;
	}
	
	@Override
	public String getName()
	{
		return "Inventory Non Tile Entity";
	}
	
	@Override
	public boolean hasCustomName()
	{
		return false;
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentString(getName());
	}
	
	@Override
	public int getSizeInventory()
	{
		return inventory.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		try
		{
			return inventory[index];
		} catch(Throwable err)
		{
		}
		return null;
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int count)
	{
		try
		{
			if(!InterItemStack.isStackNull(inventory[slot]))
			{
				ItemStack is;
				
				if(InterItemStack.getStackSize(inventory[slot]) <= count)
				{
					is = inventory[slot];
					inventory[slot] = InterItemStack.NULL_STACK;
					
					if(listener != null)
						listener.slotChange(count, inventory[slot]);
					return is;
				} else
				{
					is = inventory[slot].splitStack(count);
					if(InterItemStack.getStackSize(inventory[slot]) == 0)
						inventory[slot] = InterItemStack.NULL_STACK;
					if(listener != null)
						listener.slotChange(count, inventory[slot]);
					return is;
				}
			}
		} catch(Throwable err)
		{
		}
		return null;
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		try
		{
			inventory[index] = stack;
			if(listener != null)
				listener.slotChange(index, stack);
			if(InterItemStack.getStackSize(inventory[index]) > Math.min(inventory[index].getMaxStackSize(), getInventoryStackLimit()))
				inventory[index].stackSize = Math.min(inventory[index].getMaxStackSize(), getInventoryStackLimit());
		} catch(Throwable err)
		{
		}
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return inventoryStackLimit;
	}
	
	@Override
	public void markDirty()
	{
		writeToNBT();
	}
	
	@Override
	public void openInventory(EntityPlayer player)
	{
	}
	
	@Override
	public void closeInventory(EntityPlayer player)
	{
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return true;
	}
	
	@Override
	public int getField(int id)
	{
		return 0;
	}
	
	@Override
	public void setField(int id, int value)
	{
		
	}
	
	@Override
	public int getFieldCount()
	{
		return 0;
	}
	
	@Override
	public void clear()
	{
		for(int i = 0; i < inventory.length; ++i)
			inventory[i] = InterItemStack.NULL_STACK;
		if(listener != null)
			for(int i = 0; i < inventory.length; ++i)
				listener.slotChange(i, inventory[i]);
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		if(nbt != null)
		{
			nbt.setInteger("InvSize", this.inventory.length);
			NBTTagList list = new NBTTagList();
			for(int i = 0; i < this.inventory.length; i++)
			{
				if(!InterItemStack.isStackNull(this.inventory[i]))
				{
					NBTTagCompound tag = new NBTTagCompound();
					tag.setInteger("Slot", i);
					this.inventory[i].writeToNBT(tag);
					list.appendTag(tag);
				}
			}
			nbt.setTag("Inventory", list);
		}
	}
	
	public void writeToNBT()
	{
		writeToNBT(boundCompound);
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		if(nbt != null)
		{
			this.inventory = new ItemStack[nbt.getInteger("InvSize")];
			NBTTagList list = nbt.getTagList("Inventory", 10);
			for(int i = 0; i < list.tagCount(); i++)
			{
				NBTTagCompound tag = list.getCompoundTagAt(i);
				this.inventory[tag.getInteger("Slot")] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
	}
	
	public void readFromNBT()
	{
		readFromNBT(boundCompound);
	}
	
	public void drop(World world, BlockPos pos)
	{
		if(!world.isRemote)
		{
			for(ItemStack s : inventory)
			{
				if(InterItemStack.isStackNull(s))
					continue;
				WorldUtil.spawnItemStack(world, pos, s);
			}
		}
		
		clear();
	}
	
	@Override
	public ItemStack removeStackFromSlot(int slot)
	{
		ItemStack s = getStackInSlot(slot);
		setInventorySlotContents(slot, null);
		if(listener != null)
			listener.slotChange(slot, null);
		return s;
	}
	
	public boolean isUsableByPlayer(EntityPlayer player, BlockPos from)
	{
		return player.getDistanceSq(from) <= 64D;
	}
	
	/**
	 * use {@link #isUsableByPlayer(EntityPlayer, BlockPos)} instead.
	 */
	@Deprecated
	public boolean isUseableByPlayer(EntityPlayer player, BlockPos from)
	{
		return player.getDistanceSq(from) <= 64D;
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return false;
	}
}