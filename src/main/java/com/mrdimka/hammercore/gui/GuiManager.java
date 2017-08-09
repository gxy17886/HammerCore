package com.mrdimka.hammercore.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.gui.container.ContainerEmpty;
import com.mrdimka.hammercore.tile.TileSyncable;

public class GuiManager implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID == 0)
		{
			TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
			TileSyncable syncable = WorldUtil.cast(te, TileSyncable.class);
			
			if(syncable != null) return syncable.getServerGuiElement(player);
		}
		
		if(ID == 1) return new ContainerEmpty();
		
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID == 0)
		{
			TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
			TileSyncable syncable = WorldUtil.cast(te, TileSyncable.class);
			
			if(syncable != null) return syncable.getClientGuiElement(player);
		}
		
		if(ID == 1) return new GuiCalculator();
		return null;
	}
}