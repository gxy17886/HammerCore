package com.mrdimka.hammercore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

import com.mrdimka.hammercore.annotations.MCFBus;
import com.mrdimka.hammercore.common.items.ITooltipInjector;
import com.mrdimka.hammercore.event.GetAllRequiredApisEvent;
import com.mrdimka.hammercore.math.ExpressionEvaluator;
import com.mrdimka.hammercore.net.HCNetwork;
import com.mrdimka.hammercore.net.pkt.PacketReloadRaytracePlugins;

@MCFBus
public class TooltipAPI
{
	private static final Map<String, String> results = new HashMap<>();
	private static final Map<String, String> currentVars = new HashMap<>();
	
	@SubscribeEvent
	public void tooltipEvt(ItemTooltipEvent evt)
	{
		List<String> tooltip = evt.getToolTip();
		
		currentVars.clear();
		
		currentVars.put("count", "" + evt.getItemStack().stackSize);
		currentVars.put("damage", "" + evt.getItemStack().getItemDamage());
		currentVars.put("nbt", "" + evt.getItemStack().getTagCompound());
		
		if(evt.getItemStack().getItem() instanceof ITooltipInjector)
			((ITooltipInjector) evt.getItemStack().getItem()).injectVariables(evt.getItemStack(), currentVars);
		
		int i = 0;
		while(true)
		{
			String b = evt.getItemStack().getUnlocalizedName() + ".tooltip" + i;
			String l = I18n.translateToLocal(b);
			if(b.equals(l)) break;
			
			for(String var : currentVars.keySet()) l = l.replaceAll("&" + var, currentVars.get(var));
			
			try
			{
				if(l.contains("parse[") && l.substring(l.indexOf("parse[")).contains("]"))
				{
					String sput = "";
					String toEat = l;
					int size = 0;
					while(toEat.contains("parse[") && toEat.substring(toEat.indexOf("parse[")).contains("]"))
					{
						int parseStart = toEat.indexOf("parse[");
						sput += toEat.substring(0, parseStart);
						String fullExpr = toEat.substring(parseStart);
						fullExpr = fullExpr.substring(0, fullExpr.indexOf("]") + 1);
						String expr = fullExpr.substring(6, fullExpr.length() - 1);
						
						if(results.containsKey(expr))
						{
							sput += results.get(expr);
						}
						else
						{
							try
							{
								String res = ExpressionEvaluator.evaluate(expr);
								sput += res;
								results.put(expr, res);
							} catch(Throwable err)
							{
								l = l.replaceAll(fullExpr, "ERROR: " + expr);
							}
						}
						
						int s = parseStart + fullExpr.length();
						size += s;
						toEat = toEat.substring(s);
					}
					l = sput;
				}
			}
			catch(Throwable err) {}
			
			tooltip.add(l);
			++i;
		}
	}
	
	@SubscribeEvent
	public void getApis(GetAllRequiredApisEvent evt)
	{
		
	}
	
	@SubscribeEvent
	public void playerConnected(PlayerLoggedInEvent evt)
	{
		EntityPlayer client = HammerCore.renderProxy.getClientPlayer();
		if(client != null && client.getGameProfile().getId().equals(evt.player.getGameProfile().getId())) return;
		
		if(evt.player instanceof EntityPlayerMP) HCNetwork.manager.sendTo(new PacketReloadRaytracePlugins(), (EntityPlayerMP) evt.player);
	}
}