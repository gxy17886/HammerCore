package com.pengu.hammercore.net.pkt.script;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.pengu.hammercore.HammerCore.GRCProvider;
import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;

public class PacketSendGlobalRecipeScriptsWithRemoval implements iPacket, iPacketListener<PacketSendGlobalRecipeScriptsWithRemoval, iPacket>
{
	public int countRecipe, id;
	public NBTTagList data;
	
	public PacketSendGlobalRecipeScriptsWithRemoval()
	{
	}
	
	public PacketSendGlobalRecipeScriptsWithRemoval(int id, NBTTagList data)
	{
		countRecipe = GRCProvider.getScriptCount();
		this.id = id;
		this.data = data;
	}
	
	@Override
	public iPacket onArrived(PacketSendGlobalRecipeScriptsWithRemoval packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
		{
			GRCProvider.setScriptCount(0);
			GRCProvider.setScriptCount(packet.countRecipe);
			GRCProvider.setScript(packet.id, packet.data);
			GRCProvider.reloadScript();
			if(packet.id < GRCProvider.getScriptCount())
				return new PacketRequestRecipeScript(packet.id + 1);
		}
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("a0", countRecipe);
		nbt.setInteger("a1", id);
		nbt.setTag("t2", data);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		countRecipe = nbt.getInteger("a0");
		id = nbt.getInteger("a1");
		data = (NBTTagList) nbt.getTag("t2");
	}
}