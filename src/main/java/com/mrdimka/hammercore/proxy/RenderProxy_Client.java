package com.mrdimka.hammercore.proxy;

import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.logging.log4j.Level;

import com.mrdimka.hammercore.client.RenderGui;
import com.mrdimka.hammercore.client.renderer.IRenderHelper;
import com.mrdimka.hammercore.client.renderer.RenderHelperImpl;
import com.mrdimka.hammercore.client.renderer.item.ItemTorchRender;
import com.mrdimka.hammercore.client.renderer.tile.TileRenderMultipart;
import com.mrdimka.hammercore.client.renderer.tile.TileRenderTesseract;
import com.mrdimka.hammercore.common.blocks.multipart.TileMultipart;
import com.mrdimka.hammercore.common.blocks.tesseract.TileTesseract;
import com.mrdimka.hammercore.common.items.MultiVariantItem;
import com.mrdimka.hammercore.init.ModItems;
import com.pengu.hammercore.client.texture.SpriteSheetManager;
import com.pengu.hammercore.client.texture.TextureFXManager;
import com.pengu.hammercore.client.texture.SpriteSheetManager.SpriteSheet;
import com.pengu.hammercore.client.texture.TextureSpriteCustom;
import com.pengu.hammercore.client.texture.def.TextureSpriteAnimationFX;

@SideOnly(Side.CLIENT)
public class RenderProxy_Client extends RenderProxy_Common
{
	@Override
	public void construct()
	{
		MinecraftForge.EVENT_BUS.register(new RenderGui());
		TextureFXManager.INSTANCE.preInit();
		
		boolean RenderProxy_Client_construct_addCustomItemRenderers = false;
		
		if(RenderProxy_Client_construct_addCustomItemRenderers)
		{
			getRenderHelper().registerItemRender(Item.getItemFromBlock(Blocks.TORCH), new ItemTorchRender());
			getRenderHelper().registerItemRender(Item.getItemFromBlock(Blocks.REDSTONE_TORCH), new ItemTorchRender());
		}
	}
	
	@Override
	public void init()
	{
		//This is an example of how to make custom textures!
//		TextureSpriteCustom.createSprite(new ResourceLocation("hammercore", "builtin/animation_fx")).addTextureFX(new TextureSpriteAnimationFX(16));
		
		registerRenders(ModItems.items);
		for(MultiVariantItem multi : ModItems.multiitems)
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
	}
	
	@Override
	public IRenderHelper getRenderHelper()
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
		for(int i = DELETION_ID + messages.length - 1; i <= lastAdded; i++) chat.deleteChatLine(i);
		for(int i = 0; i < messages.length; i++) chat.printChatMessageWithOptionalDeletion(messages[i], DELETION_ID + i);
		lastAdded = DELETION_ID + messages.length - 1;
	}
	
	public static void registerRenders(Iterable<Item> items)
	{
		Iterator<Item> iter = items.iterator();
		while(iter.hasNext()) registerRender(iter.next());
	}
	
	public static void registerRender(Item item)
	{
		FMLLog.log("Hammer Core", Level.INFO, "Model definition for location " + item.getUnlocalizedName().substring(5));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(new ResourceLocation(item.getUnlocalizedName().substring(5)), "inventory"));
	}
	
	public static void registerRender(Item item, int meta, String modelName)
	{
		FMLLog.log("Hammer Core", Level.INFO, "Model definition for location " + modelName);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(new ResourceLocation(modelName), "inventory"));
	}
	
	@Override
	public World getWorld(MessageContext context)
	{
		if(context == null) return Minecraft.getMinecraft().world;
		if(context.side == Side.CLIENT) return Minecraft.getMinecraft().world;
		return super.getWorld(context);
	}
	
	@Override
	public World getWorld(MessageContext context, int dim)
	{
		if(context == null) return Minecraft.getMinecraft().world;
		if(context.side == Side.CLIENT)
		{
			World w = getWorld(context);
			if(w == null) return null;
			if(w.provider.getDimension() == dim) return w;
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