package com.mrdimka.hammercore.intr.jei;

public interface IJeiRecipeModifier
{
	void addJEI(Object recipe);
	
	void removeJEI(Object recipe);
	
	public static class Instance
	{
		public static IJeiRecipeModifier JEIModifier;
	}
}