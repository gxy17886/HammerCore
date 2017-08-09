package com.mrdimka.hammercore.common.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

/**
 * This class contains some general utilities for people
 */
public class HammerCoreUtils
{
	public static CreativeTabs createStaticIconCreativeTab(String name, final ItemStack iconStack)
	{
		return new CreativeTabs(name)
		{
			@Override
			public ItemStack getIconItemStack()
			{
				return iconStack;
			}
			
			@Override
			public Item getTabIconItem()
			{
				return iconStack.getItem();
			}
		};
	}
	
	public static CreativeTabs createDynamicCreativeTab(String name, final int delayTicks)
	{
		return new CreativeTabs(name)
		{
			private int tick = 0;
			private int index = 0;
			
			public ItemStack getIconItemStack()
			{
				List<ItemStack> items = new ArrayList<>();
				displayAllRelevantItems(items);
				if(items.size() == 0)
					return new ItemStack(Blocks.BARRIER);
				if(tick++ > delayTicks)
				{
					tick = 0;
					index++;
				}
				if(index >= items.size())
					index = 0;
				return items.get(index);
			}
			
			@Override
			public Item getTabIconItem()
			{
				return getIconItemStack().getItem();
			}
		};
	}
	
	public static AchievementPage createAchievementPage(String name, Achievement... achievements)
	{
		AchievementPage page = new AchievementPage(name, achievements);
		page.registerAchievementPage(page);
		return page;
	}
	
	public static void addAchievement(AchievementPage page, Achievement achievement)
	{
		page.getAchievements().add(achievement);
	}
	
	public static void addAchievement(AchievementPage page, String statId, String unlocalizedName, int column, int row, ItemStack stack, Achievement parent)
	{
		page.getAchievements().add(new Achievement(statId, unlocalizedName, column, row, stack, parent));
	}
}