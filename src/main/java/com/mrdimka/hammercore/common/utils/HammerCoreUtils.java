package com.mrdimka.hammercore.common.utils;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.NonNullList;
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
			public ItemStack getTabIconItem()
			{
				return iconStack;
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
				NonNullList<ItemStack> items = NonNullList.create();
				displayAllRelevantItems(items);
				if(items.size() == 0) return new ItemStack(Blocks.BARRIER);
				if(tick++ > delayTicks)
				{
					tick = 0;
					index++;
				}
				if(index >= items.size()) index = 0;
				return items.get(index);
			}

			@Override
			public ItemStack getTabIconItem()
			{
				return getIconItemStack();
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