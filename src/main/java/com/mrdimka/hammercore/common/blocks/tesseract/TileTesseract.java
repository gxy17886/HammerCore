package com.mrdimka.hammercore.common.blocks.tesseract;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import com.mrdimka.hammercore.init.ModBlocks;
import com.mrdimka.hammercore.tile.ITileDroppable;
import com.mrdimka.hammercore.tile.TileSyncableTickable;

public class TileTesseract extends TileSyncableTickable implements ITileDroppable
{
	public static final Map<String, Set<TileTesseract>> TESSERACTS = new HashMap<>();
	
	public String frequency;
	
	public TransferMode sendItems = TransferMode.DISABLED, sendFluids = TransferMode.DISABLED, sendRF = TransferMode.DISABLED, sendEJ = TransferMode.DISABLED;
	
	@Override
	public void tick()
	{
		// Put all tesseracts
		if(frequency != null && !frequency.isEmpty())
			addTesseract(this);
		
		if(atTickRate(20))
		{
			IBlockState state = world.getBlockState(pos);
			if(state.getBlock() == ModBlocks.TESSERACT)
			{
				boolean isActive = state.getValue(BlockTesseract.active);
				boolean shouldBeActive = frequency != null && !frequency.isEmpty();
				
				if(isActive != shouldBeActive)
				{
					state = state.withProperty(BlockTesseract.active, shouldBeActive);
					world.setBlockState(pos, state, 1);
					validate();
					world.setTileEntity(pos, this);
				}
			}
		}
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player)
	{
		return null;
	}
	
	@Override
	public Object getServerGuiElement(EntityPlayer player)
	{
		return null;
	}
	
	@Override
	public boolean hasGui()
	{
		return false;
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		if(frequency != null && !frequency.isEmpty())
			nbt.setString("Frequency", frequency);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		frequency = nbt.getString("Frequency");
	}
	
	public void setFrequency(String freq)
	{
		frequency = freq;
		if(freq != null && !freq.isEmpty())
			addTesseract(this);
		else
			removeTesseract(this);
	}
	
	public boolean isValid()
	{
		return world != null && pos != null && world.isBlockLoaded(pos) && world.getTileEntity(pos) == this;
	}
	
	public static void revalidateTesseracts()
	{
		Iterator<Set<TileTesseract>> tiles = TESSERACTS.values().iterator();
		
		while(tiles.hasNext())
		{
			Set<TileTesseract> tess = tiles.next();
			if(tess == null || tess.isEmpty())
			{
				tiles.remove();
				continue;
			}
			Iterator<TileTesseract> tiles2 = tess.iterator();
			while(tiles2.hasNext())
			{
				TileTesseract tess2 = tiles2.next();
				if(tess2 == null || !tess2.isValid())
					tiles2.remove();
			}
			if(tess.isEmpty())
				tiles.remove();
		}
	}
	
	public static void addTesseract(TileTesseract tesseract)
	{
		if(tesseract == null || tesseract.frequency == null || tesseract.frequency.isEmpty())
			return;
		Set<TileTesseract> tiles = TESSERACTS.get(tesseract.frequency);
		if(tiles == null)
			TESSERACTS.put(tesseract.frequency, tiles = new HashSet<>());
		tiles.add(tesseract);
	}
	
	public static void removeTesseract(TileTesseract tesseract)
	{
		Iterator<Set<TileTesseract>> tiles = TESSERACTS.values().iterator();
		
		while(tiles.hasNext())
		{
			Set<TileTesseract> tess = tiles.next();
			if(tess == null || tess.isEmpty())
			{
				tiles.remove();
				continue;
			}
			Iterator<TileTesseract> tiles2 = tess.iterator();
			while(tiles2.hasNext())
			{
				TileTesseract tess2 = tiles2.next();
				if(tess2 == tesseract)
					tiles2.remove();
			}
			if(tess.isEmpty())
				tiles.remove();
		}
	}
	
	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
		TESSERACTS.values().remove(this);
	}
	
	@Override
	public void createDrop(EntityPlayer player, World world, BlockPos pos)
	{
		TESSERACTS.values().remove(this);
	}
	
	public <T> T getSidedCapability(Capability<T> cap, EnumFacing facing)
	{
		if(facing == null)
			for(EnumFacing face : EnumFacing.VALUES)
			{
				T cap0 = getSidedCapability(cap, face);
				if(cap0 != null)
					return cap0;
			}
		
		if(isValid())
		{
			BlockPos npos = pos.offset(facing.getOpposite());
			if(world.isBlockLoaded(npos) && world.getTileEntity(npos) != null)
				return world.getTileEntity(npos).getCapability(cap, facing);
		}
		
		return null;
	}
	
	public <T> boolean hasSidedCapability(Capability<T> cap, EnumFacing facing)
	{
		if(facing == null)
			for(EnumFacing face : EnumFacing.VALUES)
			{
				boolean cap0 = hasSidedCapability(cap, face);
				if(cap0)
					return true;
			}
		
		if(isValid())
		{
			BlockPos npos = pos.offset(facing.getOpposite());
			if(world.isBlockLoaded(npos) && world.getTileEntity(npos) != null)
				return world.getTileEntity(npos).hasCapability(cap, facing);
		}
		
		return false;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return super.getCapability(capability, facing);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return super.hasCapability(capability, facing);
	}
}