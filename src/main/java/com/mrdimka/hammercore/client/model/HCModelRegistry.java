package com.mrdimka.hammercore.client.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.client.model.file.ModelFile;

@SideOnly(Side.CLIENT)
public enum HCModelRegistry
{
	INSTANCE;
	
	private HCModelRegistry() { MinecraftForge.EVENT_BUS.register(this); }
	
	public final Map<String, ModelBase> simpleModelCache = new HashMap<String, ModelBase>();
	public final Map<String, CasedModel> casedModelCache = new HashMap<String, CasedModel>();
	
	public ModelBase getModelByPath(ResourceLocation location)
	{
		return simpleModelCache.get(location + "");
	}
	
	public ModelBase getModelByPath(String path)
	{
		return simpleModelCache.get(path);
	}
	
	public CasedModel getCasedModelByPath(ResourceLocation location)
	{
		return casedModelCache.get(location + "");
	}
	
	public CasedModel getCasedModelByPath(String path)
	{
		return casedModelCache.get(path);
	}
	
	public void registerModel(String path)
	{
		simpleModelCache.put(path, null);
	}
	
	public void registerModel(ResourceLocation location)
	{
		simpleModelCache.put(location + "", null);
	}
	
	public void registerCasedModel(String path)
	{
		casedModelCache.put(path, null);
	}
	
	public void registerCasedModel(ResourceLocation location)
	{
		casedModelCache.put(location + "", null);
	}
	
	@SubscribeEvent
	public void reloadResources(TextureStitchEvent event)
	{
		reloadModels();
	}
	
	public void reloadModels()
	{
		IResourceManager mgr = Minecraft.getMinecraft().getResourceManager();
		
		int cycles = 0;
		String[] cache;
		while(true)
		{
			try
			{
				if(cycles++ >= 32) return;
				cache = simpleModelCache.keySet().toArray(new String[0]);
				break;
			}catch(Throwable err) {}
		}
		
		for(String k : cache)
		{
			try
			{
				InputStream i = mgr.getResource(new ResourceLocation(k)).getInputStream();
				simpleModelCache.put(k, SimpleModelLoader.convert(ModelFile.read(i)));
			}
			catch(Throwable err)
			{
				FMLLog.info("Model " + k + " could not be loaded:");
				err.printStackTrace();
			}
		}
	}
	
	public void reloadCasedModels()
	{
		IResourceManager mgr = Minecraft.getMinecraft().getResourceManager();
		
		int cycles = 0;
		String[] cache;
		while(true)
		{
			try
			{
				if(cycles++ >= 32) return;
				cache = casedModelCache.keySet().toArray(new String[0]);
				break;
			}catch(Throwable err) {}
		}
		
		for(String k : cache)
		{
			try
			{
				InputStream i = mgr.getResource(new ResourceLocation(k)).getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(i, "UTF-8"));
				
				String lns = "";
				String ln = br.readLine();
				while(ln != null) { lns += ln + "\n"; ln = br.readLine(); }
				
				casedModelCache.put(k, CasedModelLoader.convert(lns, false));
			}
			catch(Throwable err)
			{
				FMLLog.info("CasedModel " + k + " could not be loaded:");
				err.printStackTrace();
			}
		}
	}
}