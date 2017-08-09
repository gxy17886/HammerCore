package com.mrdimka.hammercore.net.pkt;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.net.packetAPI.IPacketListener;

public class PacketNoSpamChat implements IPacket, IPacketListener<PacketNoSpamChat, IPacket>
{
	private ITextComponent[] chatLines;
	
	public PacketNoSpamChat()
	{
		chatLines = new ITextComponent[0];
	}
	
	public PacketNoSpamChat(ITextComponent... lines)
	{
		this.chatLines = lines;
	}
	
	@Override
	public IPacket onArrived(PacketNoSpamChat packet, MessageContext context)
	{
		HammerCore.renderProxy.sendNoSpamMessages(packet.chatLines);
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("ChatLines", chatLines.length);
		int p = 0;
		for(ITextComponent c : chatLines)
		{
			nbt.setString("ChatLine" + p, ITextComponent.Serializer.componentToJson(c));
			p++;
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		chatLines = new ITextComponent[nbt.getInteger("ChatLines")];
		for(int i = 0; i < chatLines.length; i++)
			chatLines[i] = ITextComponent.Serializer.jsonToComponent(nbt.getString("ChatLine" + i));
	}
}