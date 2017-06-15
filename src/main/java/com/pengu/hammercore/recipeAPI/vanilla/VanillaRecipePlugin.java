package com.pengu.hammercore.recipeAPI.vanilla;

import com.pengu.hammercore.recipeAPI.IRecipePlugin;
import com.pengu.hammercore.recipeAPI.IRecipeTypeRegistry;
import com.pengu.hammercore.recipeAPI.RecipePlugin;
import com.pengu.hammercore.recipeAPI.vanilla.brewing.BrewingRecipeType;
import com.pengu.hammercore.recipeAPI.vanilla.furnace.SmeltingRecipeType;

@RecipePlugin
public class VanillaRecipePlugin implements IRecipePlugin
{
	@Override
	public void registerTypes(IRecipeTypeRegistry reg)
	{
		reg.register(new SmeltingRecipeType());
		reg.register(new BrewingRecipeType());
	}
}