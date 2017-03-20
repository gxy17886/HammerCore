package com.mrdimka.hammercore.recipeAPI;

import com.mrdimka.hammercore.recipeAPI.registry.IRecipeTypeRegistry;

public interface IRecipePlugin
{
	void registerTypes(IRecipeTypeRegistry reg);
}