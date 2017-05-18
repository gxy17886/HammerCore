package com.mrdimka.hammercore.recipeAPI.registry;

import net.minecraft.nbt.NBTTagCompound;

public interface IRecipeScript
{
	void add();
	
	void remove();
	
	NBTTagCompound writeToNbt();
	
	void readFromNbt(NBTTagCompound nbt);
}