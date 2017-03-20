package com.mrdimka.hammercore.recipeAPI.registry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mrdimka.hammercore.intr.jei.IJeiRecipeModifier;
import com.mrdimka.hammercore.recipeAPI.types.IRecipeType;

public class SimpleRecipeScript implements IRecipeScript
{
	public final Map<Object, IRecipeType> types = new HashMap<>();
	private final Set<Object> JEIRecipes = new HashSet<>();
	
	@Override
	public void add()
	{
		for(Object o : types.keySet())
		{
			IRecipeType type = types.get(o);
			type.addRecipe(o);
			if(type.isJeiSupported(o) && IJeiRecipeModifier.Instance.JEIModifier != null)
			{
				Object recipe = type.getJeiRecipeFor(o);
				JEIRecipes.add(recipe);
				IJeiRecipeModifier.Instance.JEIModifier.addJEI(recipe);
			}
		}
	}
	
	public void remove()
	{
		if(IJeiRecipeModifier.Instance.JEIModifier != null)
			for(Object recipe : JEIRecipes)
				IJeiRecipeModifier.Instance.JEIModifier.removeJEI(recipe);
		
		for(Object o : types.keySet())
		{
			IRecipeType type = types.get(o);
			type.removeRecipe(o);
		}
	}
}