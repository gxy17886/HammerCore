package com.pengu.hammercore.proxy;

import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.TooltipAPI;
import com.pengu.hammercore.client.RenderGui;
import com.pengu.hammercore.client.model.HasNoModel;
import com.pengu.hammercore.client.particle.iRenderHelper;
import com.pengu.hammercore.client.particle.RenderHelperImpl;
import com.pengu.hammercore.client.render.item.TileEntityItemStackRendererHC;
import com.pengu.hammercore.client.render.tile.TileRenderMultipart;
import com.pengu.hammercore.client.render.tile.TileRenderTesseract;
import com.pengu.hammercore.client.texture.TextureFXManager;
import com.pengu.hammercore.common.blocks.multipart.TileMultipart;
import com.pengu.hammercore.common.blocks.tesseract.TileTesseract;
import com.pengu.hammercore.common.items.MultiVariantItem;
import com.pengu.hammercore.init.ItemsHC;

@SideOnly(Side.CLIENT)
public class RenderProxy_Client extends RenderProxy_Common
{
	@Override
	public void construct()
	{
		MinecraftForge.EVENT_BUS.register(new RenderGui());
		MinecraftForge.EVENT_BUS.register(new TooltipAPI());
		TextureFXManager.INSTANCE.preInit();
	}
	
	@Override
	public void init()
	{
		// This is an example of how to make custom textures!
		// TextureSpriteCustom.createSprite(new ResourceLocation("hammercore",
		// "builtin/animation_fx")).addTextureFX(new
		// TextureSpriteAnimationFX(16));
		
		new TileEntityItemStackRendererHC();
		
		registerRenders(ItemsHC.items);
		for(MultiVariantItem multi : ItemsHC.multiitems)
		{
			ResourceLocation[] variants = new ResourceLocation[multi.names.length];
			for(int i = 0; i < multi.names.length; ++i)
			{
				variants[i] = new ResourceLocation(multi.names[i]);
				registerRender(multi, i, multi.names[i]);
			}
			
			ModelBakery.registerItemVariants(multi, variants);
		}
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileMultipart.class, new TileRenderMultipart());
		ClientRegistry.bindTileEntitySpecialRenderer(TileTesseract.class, new TileRenderTesseract());
		
		// try
		// {
		// JSONObject obj = (JSONObject) new JSONTokener(new
		// String(IOUtils.downloadData("https://raw.githubusercontent.com/APengu/HammerCore/1.11.x/hd_skins.json"))).nextValue();
		// for(String key : obj.keySet())
		// {
		// HammerCore.LOG.info("HD Skin for " + key + ": Loading...");
		// ResourceLocation path = new ResourceLocation("skins/" + key);
		// Minecraft.getMinecraft().getTextureManager().mapTextureObjects.put(path,
		// new URLImageTexture(path, obj.getString(key)));
		// HammerCore.LOG.info("HD Skin for " + key + ": Imported");
		// }
		// }
		// catch(Throwable err) {}
	}
	
	@Override
	public iRenderHelper getRenderHelper()
	{
		return RenderHelperImpl.INSTANCE;
	}
	
	@Override
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().player;
	}
	
	private static final int DELETION_ID = 0x16F7F6;
	private static int lastAdded;
	
	public void sendNoSpamMessages(ITextComponent[] messages)
	{
		GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
		for(int i = DELETION_ID + messages.length - 1; i <= lastAdded; i++)
			chat.deleteChatLine(i);
		for(int i = 0; i < messages.length; i++)
			chat.printChatMessageWithOptionalDeletion(messages[i], DELETION_ID + i);
		lastAdded = DELETION_ID + messages.length - 1;
	}
	
	public static void registerRenders(Iterable<Item> items)
	{
		Iterator<Item> iter = items.iterator();
		while(iter.hasNext())
			registerRender(iter.next());
	}
	
	public static void registerRender(Item item)
	{
		if(item.getClass().getAnnotation(HasNoModel.class) != null || (item instanceof ItemBlock && ((ItemBlock) item).getBlock().getClass().getAnnotation(HasNoModel.class) != null))
			return;
		HammerCore.LOG.info("Model definition for location " + item.getUnlocalizedName().substring(5));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(new ResourceLocation(item.getUnlocalizedName().substring(5)), "inventory"));
	}
	
	public static void registerRender(Item item, int meta, String modelName)
	{
		HammerCore.LOG.info("Model definition for location " + modelName);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(new ResourceLocation(modelName), "inventory"));
	}
	
	@Override
	public World getWorld(MessageContext context)
	{
		if(context == null)
			return Minecraft.getMinecraft().world;
		if(context.side == Side.CLIENT)
			return Minecraft.getMinecraft().world;
		return super.getWorld(context);
	}
	
	@Override
	public World getWorld(MessageContext context, int dim)
	{
		if(context == null)
			return Minecraft.getMinecraft().world;
		if(context.side == Side.CLIENT)
		{
			World w = getWorld(context);
			if(w == null)
				return null;
			if(w.provider.getDimension() == dim)
				return w;
		}
		return super.getWorld(context, dim);
	}
	
	@Override
	public double getBlockReachDistance_client()
	{
		return Minecraft.getMinecraft().playerController.getBlockReachDistance();
	}
	
	@Override
	public void bindTexture(ResourceLocation texture)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
	}
}