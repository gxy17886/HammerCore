package com.pengu.hammercore.init;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipesHC
{
	public static void load()
	{
		GameRegistry.addRecipe(new ShapedOreRecipe(ItemsHC.IRON_GEAR, " g ", "ggg", " g ", 'g', "ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(ItemsHC.WRENCH, " i ", " gi", "i  ", 'g', "gearIron", 'i', "ingotIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(ItemsHC.BATTERY, " g ", "iri", "iri", 'g', "gearIron", 'i', "ingotIron", 'r', "blockRedstone"));
	}
}