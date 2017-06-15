package com.pengu.hammercore.event.vanilla;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.gen.feature.WorldGenEndPodium;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.annotations.MCFBus;
import com.pengu.hammercore.api.IUpdatable;
import com.pengu.hammercore.cfg.HammerCoreConfigs;
import com.pengu.hammercore.common.HolidayTrigger;
import com.pengu.hammercore.common.utils.WorldUtil;

@MCFBus
public class SpawnEnderDragonEggHandler
{
	private static final String AUTHOR_USERNAME = "APengu", AUTHOR_DNAME = TextFormatting.BLUE + "" + TextFormatting.ITALIC + "       " + TextFormatting.RESET + "  ";
	
	@SubscribeEvent
	public void dragonDieEvent(LivingDeathEvent evt)
	{
		EntityDragon dragon = WorldUtil.cast(evt.getEntityLiving(), EntityDragon.class);
		
		dde: if(dragon != null && !dragon.world.isRemote)
		{
			if(!HammerCoreConfigs.vanilla_alwaysSpawnDragonEggs)
				break dde;
			DragonFightManager mgr = dragon.getFightManager();
			
			boolean shouldSpawnEgg = false;
			if(mgr != null)
				shouldSpawnEgg = mgr.hasPreviouslyKilledDragon();
			else
				shouldSpawnEgg = true;
			
			HammerCore.LOG.debug("We should" + (shouldSpawnEgg ? "" : "n't") + " spawn an egg.");
			
			MinecraftServer server = dragon.world.getMinecraftServer();
			
			IUpdatable SetEgg = new IUpdatable()
			{
				public int ticks = 0;
				
				@Override
				public void update()
				{
					if(ticks++ >= 601 && ticks < 1000)
					{
						WorldServer TheEnd = server.getWorld(1);
						TheEnd.getChunkFromBlockCoords(WorldGenEndPodium.END_PODIUM_LOCATION);
						if(HolidayTrigger.isHalloween())
							TheEnd.setBlockState(TheEnd.getHeight(WorldGenEndPodium.END_PODIUM_LOCATION), Blocks.LIT_PUMPKIN.getDefaultState());
						TheEnd.setBlockState(TheEnd.getHeight(WorldGenEndPodium.END_PODIUM_LOCATION), HolidayTrigger.isAprilFools() && TheEnd.rand.nextBoolean() ? Blocks.DIRT.getDefaultState() : Blocks.DRAGON_EGG.getDefaultState());
						ticks = 10000;
					}
				}
				
				@Override
				public boolean isAlive()
				{
					return ticks < 1000;
				}
			};
			
			if(shouldSpawnEgg)
				HammerCore.updatables.add(SetEgg);
		}
	}
	
	@SubscribeEvent
	public void onNameFormat(PlayerEvent.NameFormat event)
	{
		if(event.getUsername().equals(AUTHOR_USERNAME))
			event.setDisplayname(AUTHOR_DNAME);
	}
}