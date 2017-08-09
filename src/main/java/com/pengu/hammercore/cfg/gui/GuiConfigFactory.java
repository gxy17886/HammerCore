package com.pengu.hammercore.cfg.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import com.mrdimka.hammercore.HammerCore;

public class GuiConfigFactory implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance)
	{
		HammerCore.LOG.info("Created Hammer Core Gui Config Factory!");
	}
	
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}
	
	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass()
	{
		return HCConfigGui.class;
	}
	
	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
	{
		return null;
	}
}