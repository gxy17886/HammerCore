package com.pengu.hammercore.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.hammercore.utils.IndexedMap;

public interface iGuiCallback
{
	default void setGuiID(int id)
	{
		Vars.ids.put(id, this);
	}
	
	default int getGuiID()
	{
		Integer i = Vars.ids.getKey(this);
		return i != null ? i : 0;
	}
	
	Object getServerGuiElement(EntityPlayer player, World world, BlockPos pos);
	
	Object getClientGuiElement(EntityPlayer player, World world, BlockPos pos);
	
	static class Vars
	{
		private static final IndexedMap<Integer, iGuiCallback> ids = new IndexedMap<>();
	}
}