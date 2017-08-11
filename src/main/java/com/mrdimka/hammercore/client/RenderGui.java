package com.mrdimka.hammercore.client;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.api.RequiredDeps;
import com.mrdimka.hammercore.client.utils.GLImageManager;
import com.mrdimka.hammercore.client.utils.RenderUtil;
import com.mrdimka.hammercore.common.utils.IOUtils;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.gui.GuiMissingApis;
import com.mrdimka.hammercore.gui.GuiShareToLanImproved;
import com.mrdimka.hammercore.gui.smooth.GuiBrewingStandSmooth;
import com.mrdimka.hammercore.gui.smooth.GuiFurnaceSmooth;
import com.mrdimka.hammercore.json.JSONArray;
import com.mrdimka.hammercore.json.JSONObject;
import com.mrdimka.hammercore.json.JSONTokener;
import com.mrdimka.hammercore.math.ExpressionEvaluator;
import com.mrdimka.hammercore.tile.TileSyncable;
import com.pengu.hammercore.cfg.HammerCoreConfigs;
import com.pengu.hammercore.utils.IndexedMap;

@SideOnly(Side.CLIENT)
public class RenderGui
{
	private static final SpecialUser user = new SpecialUser();
	
	@SubscribeEvent
	public void guiRender(DrawScreenEvent.Post e)
	{
		GuiScreen gui = e.getGui();
		
		if(gui instanceof GuiMainMenu)
			user.draw();
	}
	
	@SubscribeEvent
	public void addF3Info(RenderGameOverlayEvent.Pre event)
	{
		if(event.getType() == ElementType.DEBUG)
			renderF3 = true;
	}
	
	private boolean renderF3;
	private final IndexedMap<String, Object> f3Right = new IndexedMap<>();
	
	@SubscribeEvent
	public void addF3Info(RenderGameOverlayEvent.Text f3)
	{
		RayTraceResult omon = Minecraft.getMinecraft().objectMouseOver;
		World world = Minecraft.getMinecraft().theWorld;
		
		if(renderF3)
		{
			List<String> tip = f3.getRight();
			if(world != null && omon != null && omon.typeOfHit == Type.BLOCK)
			{
				TileSyncable ts = WorldUtil.cast(world.getTileEntity(omon.getBlockPos()), TileSyncable.class);
				if(ts != null)
				{
					f3Right.clear();
					ts.addProperties(f3Right, omon);
					List<String> keys = f3Right.getKeys();
					for(int i = 0; i < keys.size(); ++i)
					{
						String key = keys.get(i);
						Object val = f3Right.get(key);
						String str = "";
						
						if(val instanceof Boolean)
							str = (val == Boolean.TRUE ? TextFormatting.GREEN : TextFormatting.RED) + (val + "") + TextFormatting.RESET;
						else
							str = val + "";
						
						tip.add(key.toLowerCase() + ": " + str);
					}
				}
			}
			renderF3 = false;
		}
	}
	
	@SubscribeEvent
	public void openGui(GuiOpenEvent evt)
	{
		GuiScreen gui = evt.getGui();
		final GuiScreen fgui = gui;
		
		if(gui instanceof GuiMainMenu)
		{
			new Thread(() ->
			{
				user.download();
				user.reload(true);
			}).start();
		}
		
		if(gui instanceof GuiMainMenu && !RequiredDeps.allDepsResolved())
			gui = new GuiMissingApis();
		
		if(gui instanceof GuiShareToLan && HammerCoreConfigs.client_improvedLAN)
		{
			try
			{
				Field f = gui.getClass().getDeclaredFields()[0];
				f.setAccessible(true);
				gui = new GuiShareToLanImproved((GuiScreen) f.get(gui));
			} catch(Throwable err)
			{
			}
		}
		
		smooth:
		{
			if(!HammerCoreConfigs.client_smoothVanillaGuis)
				break smooth; // Added config
				
			if(gui instanceof GuiFurnace)
			{
				try
				{
					Field[] fs = GuiFurnace.class.getDeclaredFields();
					
					Field pinv = fs[1];
					pinv.setAccessible(true);
					
					Field furn = fs[2];
					furn.setAccessible(true);
					
					InventoryPlayer playerInv = (InventoryPlayer) pinv.get(gui);
					IInventory furnaceInv = (IInventory) furn.get(gui);
					
					gui = new GuiFurnaceSmooth(playerInv, furnaceInv);
				} catch(Throwable err)
				{
					err.printStackTrace();
				}
			}
			
			if(gui instanceof GuiBrewingStand)
			{
				try
				{
					Field[] fs = GuiBrewingStand.class.getDeclaredFields();
					
					Field pinv = fs[2];
					pinv.setAccessible(true);
					
					Field bs = fs[3];
					bs.setAccessible(true);
					
					InventoryPlayer playerInv = (InventoryPlayer) pinv.get(gui);
					IInventory tbsInv = (IInventory) bs.get(gui);
					
					gui = new GuiBrewingStandSmooth(playerInv, tbsInv);
				} catch(Throwable err)
				{
				}
			}
			
			break smooth;
		}
		
		if(fgui != gui)
			evt.setGui(gui);
	}
	
	@SideOnly(Side.CLIENT)
	private static final class SpecialUser
	{
		private final int glImage = GL11.glGenTextures();
		private final List<IMG> images = new ArrayList<>();
		private long lastDownload = 0L;
		private final SecureRandom rand = new SecureRandom();
		
		private double x, y, width, height;
		private IMG currImg;
		
		private void download()
		{
			try
			{
				JSONArray arr = (JSONArray) new JSONTokener(new String(IOUtils.downloadData("http://pastebin.com/raw/ZQaapJ54"))).nextValue();
				final JSONArray ar0 = arr;
				
				for(int i = 0; i < arr.length(); ++i)
				{
					JSONObject obj = arr.getJSONObject(i);
					if(obj.optBoolean("enabled", false) && obj.getString("username").equals(Minecraft.getMinecraft().getSession().getUsername()))
					{
						arr = obj.getJSONArray("images");
						break;
					}
				}
				
				images.clear();
				
				if(arr != ar0)
					for(int i = 0; i < arr.length(); ++i)
					{
						JSONObject o = arr.getJSONObject(i);
						IMG img = new IMG();
						img.img = IOUtils.downloadPicture(o.getString("url"));
						JSONObject signature = o.getJSONObject("signature");
						img.x = signature.getString("x");
						img.y = signature.getString("y");
						img.width = signature.getString("width");
						img.height = signature.getString("height");
						images.add(img);
					}
			} catch(Throwable err)
			{
			}
		}
		
		private boolean reload(boolean launchThread)
		{
			try
			{
				if(images.isEmpty())
				{
					currImg = null;
					x = y = width = height = 0;
					return true;
				}
				
				IMG i = currImg = images.get(rand.nextInt(images.size()));
				
				String sx = i.x, sy = i.y, sw = i.width, sh = i.height;
				
				sx = format(sx);
				sy = format(sy);
				sw = format(sw);
				sh = format(sh);
				
				x = ExpressionEvaluator.evaluateDouble(sx);
				y = ExpressionEvaluator.evaluateDouble(sy);
				width = ExpressionEvaluator.evaluateDouble(sw);
				height = ExpressionEvaluator.evaluateDouble(sh);
				
				return true;
			} catch(Throwable err)
			{
				if(launchThread)
					new Thread(() ->
					{
						int i = 0;
						while(++i < 5 && !reload(false))
							;
					}).start();
			}
			
			return false;
		}
		
		private String format(String s)
		{
			if(s == null)
				return "0";
			Minecraft mc = Minecraft.getMinecraft();
			ScaledResolution sr = new ScaledResolution(mc);
			GuiScreen gs = mc.currentScreen;
			
			double displacex = sr.getScaledWidth_double() / sr.getScaledWidth();
			double displacey = sr.getScaledHeight_double() / sr.getScaledHeight();
			
			// s = s.replaceAll("mc-width", (mc.displayWidth * displacex) + "");
			s = s.replaceAll("mc-width", (mc.displayWidth) + "");
			s = s.replaceAll("mc-height", (mc.displayHeight) + "");
			// s = s.replaceAll("mc-height", (mc.displayHeight * displacey) +
			// "");
			
			return s;
		}
		
		private void draw()
		{
			if(currImg == null || currImg.img == null)
				return;
			
			if(System.currentTimeMillis() - lastDownload > 10000L)
			{
				GLImageManager.loadTexture(currImg.img, glImage, false);
				lastDownload = System.currentTimeMillis();
			}
			
			GlStateManager.bindTexture(glImage);
			
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glTranslated(x, y, 0F);
			GL11.glScaled(width, height, 1D);
			GL11.glScaled((1D / currImg.img.getWidth()), (1D / currImg.img.getHeight()), 1D);
			RenderUtil.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
		}
	}
	
	@SideOnly(Side.CLIENT)
	private static final class IMG
	{
		private BufferedImage img;
		private String x, y, width, height;
	}
}