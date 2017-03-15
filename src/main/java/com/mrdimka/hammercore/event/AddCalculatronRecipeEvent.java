package com.mrdimka.hammercore.event;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class AddCalculatronRecipeEvent extends Event
{
	public IRecipe recipe;
	
	public IRecipe getRecipe()
	{
		return recipe;
	}
	
	public void setRecipe(IRecipe recipe)
	{
		this.recipe = recipe;
	}
	
	@Override
	public boolean isCancelable()
	{
		return true;
	}
}