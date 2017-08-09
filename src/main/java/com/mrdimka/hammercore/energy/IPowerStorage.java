package com.mrdimka.hammercore.energy;

public interface IPowerStorage
{
	public int receiveEnergy(int maxReceive, boolean simulate);
	
	public int extractEnergy(int maxExtract, boolean simulate);
	
	public int getEnergyStored();
	
	public int getMaxEnergyStored();
}