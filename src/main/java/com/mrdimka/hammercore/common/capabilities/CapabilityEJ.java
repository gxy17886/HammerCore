package com.mrdimka.hammercore.common.capabilities;

import java.util.concurrent.Callable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import com.mrdimka.hammercore.energy.IPowerStorage;
import com.mrdimka.hammercore.energy.PowerStorage;

public class CapabilityEJ
{
	@CapabilityInject(IPowerStorage.class)
    public static Capability<IPowerStorage> ENERGY = null;

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IPowerStorage.class, new IStorage<IPowerStorage>()
        {
            @Override
            public NBTBase writeNBT(Capability<IPowerStorage> capability, IPowerStorage instance, EnumFacing side)
            {
                return new NBTTagInt(instance.getEnergyStored());
            }

            @Override
            public void readNBT(Capability<IPowerStorage> capability, IPowerStorage instance, EnumFacing side, NBTBase nbt)
            {
                if (!(instance instanceof PowerStorage))
                    throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
                ((PowerStorage)instance).energy = ((NBTTagInt)nbt).getInt();
            }
        },
        new Callable<IPowerStorage>()
        {
            @Override
            public IPowerStorage call() throws Exception
            {
                return new PowerStorage(1000);
            }
        });
    }
}