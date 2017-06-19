package com.pengu.hammercore.var;

import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.net.packetAPI.IPacketListener;

public class PacketUpdateDirtyVariables implements IPacket, IPacketListener<PacketUpdateDirtyVariables, IPacket>
{
	public NBTTagList nbt;
	
	public PacketUpdateDirtyVariables(Map<String, IVariable> vars)
	{
		nbt = new NBTTagList();
		for(String key : vars.keySet())
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("Id", key);
			tag.setTag("Data", vars.get(key).writeToNBT(new NBTTagCompound()));
			nbt.appendTag(tag);
		}
	}
	
	public PacketUpdateDirtyVariables()
	{
	}
	
	@Override
	public IPacket onArrived(PacketUpdateDirtyVariables packet, MessageContext context)
	{
		NBTTagList list = packet.nbt;
		for(int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound nbt = list.getCompoundTagAt(i);
			IVariable var = VariableManager.getVariable(nbt.getString("Id"));
			if(var != null)
				var.readFromNBT(nbt.getCompoundTag("Data"));
		}
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("List", this.nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.nbt = nbt.getTagList("List", NBT.TAG_COMPOUND);
	}
}