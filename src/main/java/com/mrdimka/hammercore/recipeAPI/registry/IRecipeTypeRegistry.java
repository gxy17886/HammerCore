package com.mrdimka.hammercore.recipeAPI.registry;

import com.mrdimka.hammercore.recipeAPI.types.IRecipeType;

public interface IRecipeTypeRegistry
{
	void register(IRecipeType type);
}