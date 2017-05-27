package com.mrdimka.hammercore.recipeAPI.vanilla.brewing;

import java.util.Arrays;

import mezz.jei.plugins.vanilla.brewing.BrewingRecipeWrapper;
import net.minecraft.nbt.NBTTagCompound;

import com.mrdimka.hammercore.common.match.item.ItemListContainerHelper;
import com.mrdimka.hammercore.common.match.item.ItemMatchParams;
import com.mrdimka.hammercore.recipeAPI.BrewingRecipe;
import com.mrdimka.hammercore.recipeAPI.types.IRecipeType;

public class BrewingRecipeType implements IRecipeType<BrewingRecipeHC>
{
	@Override
	public boolean isJeiSupported(BrewingRecipeHC recipe)
	{
		return true;
	}
	
	@Override
	public Object getJeiRecipeFor(BrewingRecipeHC recipe, boolean remove)
	{
		return new BrewingRecipeWrapper(recipe.ingredients, recipe.input, recipe.output, 0);
	}
	
	@Override
	public String getTypeId()
	{
		return "brewing";
	}
	
	@Override
	public BrewingRecipeHC createRecipe(NBTTagCompound json)
	{
		BrewingRecipeHC r = new BrewingRecipeHC();
		
		r.input = loadStacks(json.getTag("input"), "input").get(0);
		r.ingredients.addAll(loadStacks(json.getTag("ingredients"), "ingredients"));
		r.output = loadStack(json.getCompoundTag("output"));
		
		return r;
	}
	
	@Override
	public void addRecipe(BrewingRecipeHC recipe)
	{
		ItemMatchParams params = new ItemMatchParams().setUseOredict(true);
		recipe.recipeInstance = BrewingRecipe.INSTANCE.addRecipe(ItemListContainerHelper.stackPredicate(Arrays.asList(recipe.input), params), ItemListContainerHelper.stackPredicate(recipe.ingredients, params), recipe.output);
	}
	
	@Override
	public void removeRecipe(BrewingRecipeHC recipe)
	{
		BrewingRecipe.INSTANCE.recipes.remove(recipe.recipeInstance);
	}
}