package com.pengu.hammercore.net.pkt.script;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.pengu.hammercore.HammerCore.GRCProvider;
import com.pengu.hammercore.net.packetAPI.IPacket;
import com.pengu.hammercore.net.packetAPI.IPacketListener;

public class PacketRequestRecipeScript implements IPacket, IPacketListener<PacketRequestRecipeScript, IPacket>
{
	public int id;
	
	public PacketRequestRecipeScript()
	{
	}
	
	public PacketRequestRecipeScript(int id)
	{
		this.id = id;
	}
	
	@Override
	public IPacket onArrived(PacketRequestRecipeScript packet, MessageContext context)
	{
		if(context.side == Side.SERVER && GRCProvider.getScriptCount() > packet.id)
			return new PacketSendGlobalRecipeScripts(packet.id, GRCProvider.getScript(packet.id));
		else if(context.side == Side.SERVER)
			return new PacketReloadScripts();
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("a0", id);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		id = nbt.getInteger("a0");
	}
}