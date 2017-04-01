package com.mrdimka.hammercore.recipeAPI.vanilla;

import com.mrdimka.hammercore.recipeAPI.IRecipePlugin;
import com.mrdimka.hammercore.recipeAPI.RecipePlugin;
import com.mrdimka.hammercore.recipeAPI.registry.IRecipeTypeRegistry;
import com.mrdimka.hammercore.recipeAPI.vanilla.brewing.BrewingRecipeType;
import com.mrdimka.hammercore.recipeAPI.vanilla.crafting.ShapedRecipeType;
import com.mrdimka.hammercore.recipeAPI.vanilla.crafting.ShapelessRecipeType;
import com.mrdimka.hammercore.recipeAPI.vanilla.furnace.SmeltingRecipeType;

@RecipePlugin
public class VanillaRecipePlugin implements IRecipePlugin
{
	@Override
	public void registerTypes(IRecipeTypeRegistry reg)
	{
		reg.register(new ShapedRecipeType());
		reg.register(new ShapelessRecipeType());
		reg.register(new SmeltingRecipeType());
		reg.register(new BrewingRecipeType());
	}
}