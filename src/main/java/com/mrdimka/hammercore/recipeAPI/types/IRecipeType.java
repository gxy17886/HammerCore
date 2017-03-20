package com.mrdimka.hammercore.recipeAPI.types;

import com.mrdimka.hammercore.common.utils.StringToItemStack;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IRecipeType<T>
{
	/**
	 * Indicates if HC should add/remove created instance of recipe to JEI.
	 */
	boolean isJeiSupported(T recipe);
	
	/**
	 * Returns the recipe that should be added to JEIRecipeRegistry
	 */
	Object getJeiRecipeFor(T recipe);
	
	String getTypeId();
	
	T createRecipe(NBTTagCompound json);
	
	void addRecipe(T recipe);
	void removeRecipe(T recipe);
	
	default ItemStack parseStack(String stack, String nbt)
	{
		return StringToItemStack.toItemStack(stack, nbt);
	}
	
	default ItemStack loadStack(NBTTagCompound nbt)
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setString("id", nbt.getString("id"));
		
		if(nbt.hasKey("count")) compound.setByte("Count", (byte) nbt.getInteger("count"));
		else compound.setByte("Count", (byte) 1);
		
		if(nbt.hasKey("damage")) compound.setShort("Damage", (short) nbt.getInteger("damage"));
		if(nbt.hasKey("tag")) compound.setTag("tag", nbt.getCompoundTag("tag"));
		if(nbt.hasKey("ForgeCaps")) compound.setTag("ForgeCaps", nbt.getCompoundTag("ForgeCaps"));
		return new ItemStack(compound);
	}
	
	public static class RecipeParseException extends RuntimeException
	{
		public RecipeParseException(String msg)
		{
			super(msg);
		}
	}
}