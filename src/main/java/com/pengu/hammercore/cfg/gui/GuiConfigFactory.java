package com.pengu.hammercore.cfg.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import com.pengu.hammercore.HammerCore;

public class GuiConfigFactory implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance)
	{
		HammerCore.LOG.info("Created Hammer Core Gui Config Factory!");
	}
	
	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass()
	{
		return HCConfigGui.class;
	}
	
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}
	
	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
	{
		return null;
	}

	@Override
    public boolean hasConfigGui()
    {
	    return true;
    }

	@Override
    public GuiScreen createConfigGui(GuiScreen parentScreen)
    {
	    return new HCConfigGui(parentScreen);
    }
}