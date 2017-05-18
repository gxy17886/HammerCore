package com.mrdimka.hammercore.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IGuiCallback
{
	Vars vars = new Vars();
	
	default void setGuiID(int id)
	{
		vars.id = id;
	}
	
	default int getGuiID()
	{
		return vars.id;
	}
	
	Object getServerGuiElement(EntityPlayer player, World world, BlockPos pos);
	
	Object getClientGuiElement(EntityPlayer player, World world, BlockPos pos);
	
	static class Vars
	{
		private int id;
	}
}