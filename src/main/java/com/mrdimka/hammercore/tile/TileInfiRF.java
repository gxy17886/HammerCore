package com.mrdimka.hammercore.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import com.mrdimka.hammercore.common.capabilities.CapabilityEJ;
import com.mrdimka.hammercore.energy.IPowerStorage;
import com.mrdimka.hammercore.ext.TeslaAPI;

public class TileInfiRF extends TileSyncableTickable implements IEnergyStorage, IPowerStorage
{
	@Override
	public void tick()
	{
		for(EnumFacing f : EnumFacing.VALUES)
		{
			TileEntity t = worldObj.getTileEntity(pos.offset(f));
			
			if(t == null) continue;
			
			if(TeslaAPI.isTeslaConsumer(t)) TeslaAPI.givePowerToConsumer(t, Long.MAX_VALUE, false);
			
			if(t.hasCapability(CapabilityEnergy.ENERGY, f.getOpposite()))
			{
				IEnergyStorage storage = t.getCapability(CapabilityEnergy.ENERGY, f.getOpposite());
				if(storage != null && storage.canReceive()) storage.receiveEnergy(Integer.MAX_VALUE, false);
			}
			
			if(t.hasCapability(CapabilityEJ.ENERGY, f.getOpposite()))
			{
				IPowerStorage storage = t.getCapability(CapabilityEJ.ENERGY, f.getOpposite());
				if(storage != null) storage.receiveEnergy(Integer.MAX_VALUE, false);
			}
		}
	}
	
	public void writeNBT(NBTTagCompound nbt) {};
	public void readNBT(NBTTagCompound nbt) {}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		return 0;
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		return maxExtract;
	}
	
	@Override
	public int getEnergyStored()
	{
		return Integer.MAX_VALUE;
	}
	
	@Override
	public int getMaxEnergyStored()
	{
		return Integer.MAX_VALUE;
	}
	
	@Override
	public boolean canExtract()
	{
		return true;
	}
	
	@Override
	public boolean canReceive()
	{
		return false;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability == CapabilityEJ.ENERGY || capability == CapabilityEnergy.ENERGY) return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityEJ.ENERGY || capability == CapabilityEnergy.ENERGY) return (T) this;
		return super.getCapability(capability, facing);
	}
}