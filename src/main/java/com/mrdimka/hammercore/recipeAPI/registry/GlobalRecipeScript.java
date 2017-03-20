package com.mrdimka.hammercore.recipeAPI.registry;

public class GlobalRecipeScript implements IRecipeScript
{
	public final IRecipeScript[] scripts;
	
	public GlobalRecipeScript(IRecipeScript... sub)
	{
		scripts = sub;
	}
	
	@Override
	public void add()
	{
		for(IRecipeScript s : scripts)
			s.add();
	}
	
	public void remove()
	{
		for(IRecipeScript s : scripts)
			s.remove();
	}
}