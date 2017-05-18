package com.mrdimka.hammercore.recipeAPI.registry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.intr.jei.IJeiRecipeModifier;
import com.mrdimka.hammercore.recipeAPI.types.IRecipeType;

public class SimpleRecipeScript implements IRecipeScript
{
	public final Map<Object, IRecipeType> types = new HashMap<>();
	public final Set<Object> swaps = new HashSet<>();
	public NBTTagList makeTag;
	private final Set<Object> JEIRecipesAdded = new HashSet<>();
	private final Set<Object> JEIRecipesRemoved = new HashSet<>();
	
	@Override
	public void add()
	{
		for(Object o : types.keySet())
		{
			IRecipeType type = types.get(o);
			if(swaps.contains(o) && type.swapAddRemoveSupported(o))
			{
				type.removeOnLoad(o);
				if(type.isJeiSupported(o) && IJeiRecipeModifier.Instance.JEIModifier != null)
				{
					Object recipe = type.getJeiRecipeFor(o, true);
					JEIRecipesRemoved.add(recipe);
					IJeiRecipeModifier.Instance.JEIModifier.removeJEI(recipe);
				}
			} else
			{
				if(swaps.contains(o))
					HammerCore.LOG.warn("Found recipe to remove but it doesn't support remove reverse operation!");
				
				type.addRecipe(o);
				if(type.isJeiSupported(o) && IJeiRecipeModifier.Instance.JEIModifier != null)
				{
					Object recipe = type.getJeiRecipeFor(o, false);
					JEIRecipesAdded.add(recipe);
					IJeiRecipeModifier.Instance.JEIModifier.addJEI(recipe);
				}
			}
		}
	}
	
	public void remove()
	{
		if(IJeiRecipeModifier.Instance.JEIModifier != null)
		{
			for(Object recipe : JEIRecipesAdded)
				IJeiRecipeModifier.Instance.JEIModifier.removeJEI(recipe);
			for(Object recipe : JEIRecipesRemoved)
				IJeiRecipeModifier.Instance.JEIModifier.addJEI(recipe);
		}
		
		for(Object o : types.keySet())
		{
			IRecipeType type = types.get(o);
			if(type.swapAddRemoveSupported(o))
				type.addOnUnload(o);
			else
				type.removeRecipe(o);
		}
	}
	
	@Override
	public NBTTagCompound writeToNbt()
	{
		return null;
	}
	
	@Override
	public void readFromNbt(NBTTagCompound nbt)
	{
		
	}
}