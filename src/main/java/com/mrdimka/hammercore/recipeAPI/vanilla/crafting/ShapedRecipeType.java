package com.mrdimka.hammercore.recipeAPI.vanilla.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.mrdimka.hammercore.recipeAPI.types.IRecipeType;

public class ShapedRecipeType implements IRecipeType<ShapedOreRecipe>
{
	@Override
	public boolean isJeiSupported(ShapedOreRecipe recipe)
	{
		return true;
	}
	
	@Override
	public Object getJeiRecipeFor(ShapedOreRecipe recipe)
	{
		return recipe;
	}
	
	@Override
	public String getTypeId()
	{
		return "shaped_recipe";
	}
	
	@Override
	public ShapedOreRecipe createRecipe(NBTTagCompound json)
	{
		ItemStack out = loadStack(json.getCompoundTag("output"));
		List<Object> data = new ArrayList<>();
		NBTTagList pattern = json.getTagList("pattern", NBT.TAG_STRING);
		for(int i = 0; i < pattern.tagCount(); ++i)
			data.add(pattern.getStringTagAt(i));
		NBTTagCompound ingredients = json.getCompoundTag("ingredients");
		for(String item : ingredients.getKeySet())
		{
			data.add(item.charAt(0));
			if(ingredients.hasKey(item, NBT.TAG_STRING))
				data.add(ingredients.getString(item));
			else if(ingredients.hasKey(item, NBT.TAG_COMPOUND))
				data.add(loadStack(ingredients.getCompoundTag(item)));
			else
				throw new RecipeParseException("Undefined type for ingredient '" + item + "': TagID: " + ingredients.getTagId(item) + ", Content: " + ingredients.getTag(item));
		}
		return new ShapedOreRecipe(out, data.toArray());
	}
	
	@Override
	public void addRecipe(ShapedOreRecipe recipe)
	{
		CraftingManager.getInstance().addRecipe(recipe);
	}
	
	@Override
	public void removeRecipe(ShapedOreRecipe recipe)
	{
		CraftingManager.getInstance().getRecipeList().remove(recipe);
	}
}