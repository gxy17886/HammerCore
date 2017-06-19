package com.pengu.hammercore.init;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.oredict.OreDictionary;

import com.google.common.collect.Maps;
import com.pengu.hammercore.api.INoItemBlock;
import com.pengu.hammercore.api.ITileBlock;
import com.pengu.hammercore.api.multipart.BlockMultipartProvider;
import com.pengu.hammercore.common.blocks.IItemBlock;
import com.pengu.hammercore.common.items.MultiVariantItem;
import com.pengu.hammercore.utils.IRegisterListener;
import com.pengu.hammercore.utils.SoundObject;

public class SimpleRegistration
{
	private static final List<IRecipe> recipes = new ArrayList<>();
	
	public static void implementRecipe(IRecipe recipe)
	{
		recipes.add(recipe);
	}
	
	public static void registerRegisteredRecipes(IForgeRegistry<IRecipe> reg)
	{
		reg.registerAll(recipes.toArray(new IRecipe[recipes.size()]));
	}
	
	/**
	 * Registers a recipe. This was implemented because of major recipe change.
	 * Use this to register in-code recipes, but I HIGHLY RECCOMEND TO MOVE TO
	 * JSON!
	 * 
	 * @param name
	 *            The name of the recipe. ModID is pre-pended to it.
	 * @param stack
	 *            The output of the recipe.
	 * @param recipeComponents
	 *            The recipe components.
	 */
	public static void addShapedRecipe(String name, ItemStack stack, Object... recipeComponents)
	{
		ModContainer mc = Loader.instance().activeModContainer();
		name = (mc != null ? mc.getModId() : "hammercore") + ":" + name;
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;
		
		if(recipeComponents[i] instanceof String[])
		{
			String[] astring = (String[]) ((String[]) recipeComponents[i++]);
			
			for(String s2 : astring)
			{
				++k;
				j = s2.length();
				s = s + s2;
			}
		} else
		{
			while(recipeComponents[i] instanceof String)
			{
				String s1 = (String) recipeComponents[i++];
				++k;
				j = s1.length();
				s = s + s1;
			}
		}
		
		Map<Character, ItemStack[]> map;
		
		for(map = Maps.<Character, ItemStack[]> newHashMap(); i < recipeComponents.length; i += 2)
		{
			Character character = (Character) recipeComponents[i];
			List<ItemStack> itemstack = new ArrayList<ItemStack>();
			
			if(recipeComponents[i + 1] instanceof Item)
				itemstack.add(new ItemStack((Item) recipeComponents[i + 1]));
			else if(recipeComponents[i + 1] instanceof Block)
				itemstack.add(new ItemStack((Block) recipeComponents[i + 1], 1, OreDictionary.WILDCARD_VALUE));
			else if(recipeComponents[i + 1] instanceof ItemStack)
				itemstack.add(((ItemStack) recipeComponents[i + 1]).copy());
			else if(recipeComponents[i + 1] instanceof String)
				itemstack.addAll(OreDictionary.getOres(recipeComponents[i + 1] + ""));
			
			map.put(character, itemstack.toArray(new ItemStack[0]));
		}
		
		NonNullList<Ingredient> aitemstack = NonNullList.withSize(j * k, Ingredient.EMPTY);
		
		for(int l = 0; l < j * k; ++l)
		{
			char c0 = s.charAt(l);
			
			if(map.containsKey(Character.valueOf(c0)))
				aitemstack.set(l, Ingredient.fromStacks(map.get(Character.valueOf(c0))));
		}
		
		implementRecipe(new ShapedRecipes(name, j, k, aitemstack, stack));
	}
	
	/**
	 * This should only be used for registering recipes for vanilla objects and
	 * not mod-specific objects.
	 * 
	 * @param name
	 *            The name of the recipe.
	 * @param stack
	 *            The output stack.
	 * @param recipeComponents
	 *            The recipe components.
	 */
	public static void addShapelessRecipe(String name, ItemStack stack, Object... recipeComponents)
	{
		ModContainer mc = Loader.instance().activeModContainer();
		name = (mc != null ? mc.getModId() : "hammercore") + ":" + name;
		NonNullList<Ingredient> list = NonNullList.create();
		
		for(Object object : recipeComponents)
		{
			if(object instanceof ItemStack)
				list.add(Ingredient.fromStacks(((ItemStack) object).copy()));
			else if(object instanceof Item)
				list.add(Ingredient.fromStacks(new ItemStack((Item) object)));
			else if(object instanceof String)
				list.add(Ingredient.fromStacks(OreDictionary.getOres(object + "").toArray(new ItemStack[0])));
			else
			{
				if(!(object instanceof Block))
					throw new IllegalArgumentException("Invalid shapeless recipe: unknown type " + object.getClass().getName() + "!");
				list.add(Ingredient.fromStacks(new ItemStack((Block) object)));
			}
		}
		
		implementRecipe(new ShapelessRecipes(name, stack, list));
	}
	
	public static void registerFieldItemsFrom(Class<?> owner, String modid, CreativeTabs tab)
	{
		Field[] fs = owner.getDeclaredFields();
		for(Field f : fs)
			if(Item.class.isAssignableFrom(f.getType()))
				try
				{
					f.setAccessible(true);
					registerItem((Item) f.get(null), modid, tab);
				} catch(Throwable err)
				{
				}
	}
	
	public static void registerFieldBlocksFrom(Class<?> owner, String modid, CreativeTabs tab)
	{
		Field[] fs = owner.getDeclaredFields();
		for(Field f : fs)
			if(Block.class.isAssignableFrom(f.getType()))
				try
				{
					f.setAccessible(true);
					registerBlock((Block) f.get(null), modid, tab);
				} catch(Throwable err)
				{
				}
	}
	
	public static void registerFieldSoundsFrom(Class<?> owner)
	{
		Field[] fs = owner.getDeclaredFields();
		for(Field f : fs)
			if(SoundObject.class.isAssignableFrom(f.getType()))
				try
				{
					f.setAccessible(true);
					registerSound((SoundObject) f.get(null));
				} catch(Throwable err)
				{
				}
	}
	
	/**
	 * Registers {@link SoundObject} to registry and populates
	 * {@link SoundObject} with {@link SoundEvent}.
	 **/
	public static void registerSound(SoundObject sound)
	{
		sound.sound = GameRegistry.register(sound.sound = new SoundEvent(sound.name).setRegistryName(sound.name));
	}
	
	public static void registerItem(Item item, String modid, CreativeTabs tab)
	{
		if(item == null)
			return;
		String name = item.getUnlocalizedName().substring("item.".length());
		item.setRegistryName(modid, name);
		item.setUnlocalizedName(modid + ":" + name);
		if(tab != null)
			item.setCreativeTab(tab);
		GameRegistry.register(item);
		if(item instanceof IRegisterListener)
			((IRegisterListener) item).onRegistered();
		if(item instanceof MultiVariantItem)
			ModItems.multiitems.add((MultiVariantItem) item);
		else
			ModItems.items.add(item);
	}
	
	public static void registerBlock(Block block, String modid, CreativeTabs tab)
	{
		if(block == null)
			return;
		String name = block.getUnlocalizedName().substring("tile.".length());
		block.setUnlocalizedName(modid + ":" + name);
		block.setCreativeTab(tab);
		
		// ItemBlockDefinition
		Item ib = null;
		
		if(block instanceof BlockMultipartProvider)
			ib = ((BlockMultipartProvider) block).createItem();
		else if(block instanceof IItemBlock)
			ib = ((IItemBlock) block).getItemBlock();
		else
			ib = new ItemBlock(block);
		
		GameRegistry.register(block, new ResourceLocation(modid, name));
		if(!(block instanceof INoItemBlock))
			GameRegistry.register(ib.setRegistryName(block.getRegistryName()));
		
		if(block instanceof IRegisterListener)
			((IRegisterListener) block).onRegistered();
		
		if(block instanceof ITileBlock)
		{
			Class c = ((ITileBlock) block).getTileClass();
			
			// Better registration of tiles. Maybe this will fix tile
			// disappearing?
			GameRegistry.registerTileEntity(c, modid + ":" + c.getName().substring(c.getName().lastIndexOf(".") + 1).toLowerCase());
		} else if(block instanceof ITileEntityProvider)
		{
			ITileEntityProvider te = (ITileEntityProvider) block;
			TileEntity t = te.createNewTileEntity(null, 0);
			if(t != null)
			{
				Class c = t.getClass();
				GameRegistry.registerTileEntity(c, modid + ":" + c.getName().substring(c.getName().lastIndexOf(".") + 1).toLowerCase());
			}
		}
		
		if(!(block instanceof INoItemBlock))
		{
			Item i = Item.getItemFromBlock(block);
			if(i instanceof IRegisterListener)
				((IRegisterListener) i).onRegistered();
			if(i instanceof MultiVariantItem)
				ModItems.multiitems.add((MultiVariantItem) i);
			else if(i != null)
				ModItems.items.add(i);
		}
	}
}