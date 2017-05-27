package com.mrdimka.hammercore.recipeAPI.vanilla.brewing;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.mrdimka.hammercore.recipeAPI.BrewingRecipe.BSR;

public class BrewingRecipeHC
{
	public BSR recipeInstance;
	public ItemStack input, output;
	public final List<ItemStack> ingredients = new ArrayList<>();
}