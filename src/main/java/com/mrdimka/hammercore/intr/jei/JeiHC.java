package com.mrdimka.hammercore.intr.jei;

import com.mrdimka.hammercore.gui.smooth.GuiBrewingStandSmooth;
import com.mrdimka.hammercore.gui.smooth.GuiFurnaceSmooth;

import net.minecraft.client.gui.inventory.GuiFurnace;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;

@JEIPlugin
public class JeiHC implements IModPlugin, IJeiRecipeModifier
{
	IRecipeRegistry registry;
	
	{
		Instance.JEIModifier = this;
	}
	
	@Override
	public void onRuntimeAvailable(IJeiRuntime runtime)
	{
		registry = runtime.getRecipeRegistry();
	}
	
	@Override
	public void register(IModRegistry reg)
	{
		//Add click areas to our smotth guis
		reg.addRecipeClickArea(GuiBrewingStandSmooth.class, 97, 16, 14, 30, "minecraft.brewing");
		reg.addRecipeClickArea(GuiFurnaceSmooth.class, 78, 32, 28, 23, "minecraft.smelting");
	}
	
	@Override
	public void registerIngredients(IModIngredientRegistration arg0)
	{
	}
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistry arg0)
	{
	}

	@Override
	public void addJEI(Object recipe)
	{
		registry.addRecipe(recipe);
	}
	
	@Override
	public void removeJEI(Object recipe)
	{
		registry.removeRecipe(recipe);
	}
}