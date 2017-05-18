package com.mrdimka.hammercore.recipeAPI.vanilla.crafting;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.mrdimka.hammercore.recipeAPI.types.IRecipeType;

public class ShapelessRecipeType implements IRecipeType<ShapelessOreRecipe>
{
	@Override
	public boolean isJeiSupported(ShapelessOreRecipe recipe)
	{
		return true;
	}
	
	@Override
	public Object getJeiRecipeFor(ShapelessOreRecipe recipe)
	{
		return recipe;
	}
	
	@Override
	public String getTypeId()
	{
		return "shapeless_recipe";
	}
	
	@Override
	public ShapelessOreRecipe createRecipe(NBTTagCompound json)
	{
		ItemStack out = loadStack(json.getCompoundTag("output"));
		List<Object> data = new ArrayList<>();
		
		NBTTagList ingredients = json.getTagList("ingredients", NBT.TAG_COMPOUND);
		for(int i = 0; i < ingredients.tagCount(); ++i)
		{
			NBTTagCompound ing = ingredients.getCompoundTagAt(i);
			String item = "item";
			
			if(ing.hasKey(item, NBT.TAG_STRING))
				data.add(ing.getString(item));
			else if(ing.hasKey(item, NBT.TAG_COMPOUND))
				data.add(loadStack(ing.getCompoundTag(item)));
			else
				throw new RecipeParseException("Undefined type for ingredient '" + i + "': TagID: " + ing.getTagId(item) + ", Content: " + ing.getTag(item));
		}
		
		return new ShapelessOreRecipe(out, data.toArray());
	}
	
	@Override
	public void addRecipe(ShapelessOreRecipe recipe)
	{
		CraftingManager.getInstance().addRecipe(recipe);
	}
	
	@Override
	public void removeRecipe(ShapelessOreRecipe recipe)
	{
		CraftingManager.getInstance().getRecipeList().remove(recipe);
	}
}