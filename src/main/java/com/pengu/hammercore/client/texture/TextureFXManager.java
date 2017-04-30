package com.pengu.hammercore.client.texture;

import java.util.Map;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.event.TextureStitchEvent.Post;
import net.minecraftforge.client.event.TextureStitchEvent.Pre;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.google.common.collect.Maps;

public enum TextureFXManager
{
	INSTANCE;
    private Map<String, TextureSpriteCustom> iconMap = Maps.newHashMap();
	
	public void preInit()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void registerIcon(String path, TextureSpriteCustom sprite)
	{
		if(!this.iconMap.containsKey(path))
			this.iconMap.put(path, sprite);
	}
	
	/** upload and reload all custom textures */
	@SubscribeEvent
	public void onTextureStitch(TextureStitchEvent event)
	{
		for(TextureSpriteCustom s : iconMap.values()) s.resetFX(event instanceof Pre ? 0 : event instanceof Post ? 1 : 2);
		event.getMap().mapUploadedSprites.putAll(iconMap);
	}
}