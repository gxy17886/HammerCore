package com.mrdimka.hammercore.event;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

import com.mrdimka.hammercore.annotations.MCFBus;
import com.mrdimka.hammercore.common.blocks.tesseract.TileTesseract;
import com.mrdimka.hammercore.tile.ITileDroppable;
import com.mrdimka.hammercore.tile.TileSyncable;

@MCFBus
public class TileHandler
{
	@SubscribeEvent
	public void breakBlock(BlockEvent.BreakEvent evt)
	{
		IBlockState state = evt.getState();
		if(state.getBlock() instanceof ITileDroppable)
			((ITileDroppable) state.getBlock()).createDrop(evt.getPlayer(), evt.getWorld(), evt.getPos());
		TileEntity te = evt.getWorld().getTileEntity(evt.getPos());
		if(te instanceof ITileDroppable)
			((ITileDroppable) te).createDrop(evt.getPlayer(), evt.getWorld(), evt.getPos());
	}
	
	@SubscribeEvent
	public void breakBlock(BlockEvent.PlaceEvent evt)
	{
		TileEntity te = evt.getWorld().getTileEntity(evt.getPos());
		
		if(te == null)
		{
			Block block = evt.getPlacedBlock().getBlock();
			if(block instanceof ITileEntityProvider)
			{
				te = block.createTileEntity(evt.getWorld(), evt.getPlacedBlock());
				evt.getWorld().setTileEntity(evt.getPos(), te);
			}
		}
		
		if(te instanceof TileSyncable)
		{
			TileSyncable tile = (TileSyncable) te;
			tile.onPlacedBy(evt.getPlayer(), evt.getHand());
		}
	}
	
	private long lastCheck;
	
	@SubscribeEvent
	public void serverTick(ServerTickEvent evt)
	{
		long currentCheck = System.currentTimeMillis() / 50L;
		
		if(currentCheck - lastCheck > 40)
		{
//			TileTesseract.revalidateTesseracts();
			lastCheck = currentCheck;
		}
	}
}