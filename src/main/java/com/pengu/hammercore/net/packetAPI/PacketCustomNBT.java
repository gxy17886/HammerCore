package com.pengu.hammercore.net.packetAPI;

import io.netty.buffer.ByteBuf;

import java.lang.reflect.Constructor;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketCustomNBT implements IMessage, IMessageHandler<PacketCustomNBT, IMessage>
{
	public NBTTagCompound nbt = new NBTTagCompound();
	
	PacketCustomNBT(iPacket packet, String channel)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		packet.writeToNBT(nbt);
		this.nbt.setTag("PacketData", nbt);
		this.nbt.setString("PacketClass", packet.getClass().getName());
		this.nbt.setString("Channel", channel);
	}
	
	public PacketCustomNBT()
	{
	}
	
	@Override
	public IMessage onMessage(PacketCustomNBT message, MessageContext ctx)
	{
		try
		{
			NBTTagCompound nbt = message.nbt;
			PacketManager mgr = PacketManager.getManagerByChannel(message.nbt.getString("Channel"));
			Class<iPacket> packetClass = (Class<iPacket>) Class.forName(nbt.getString("PacketClass"));
			Constructor<iPacket> contr = packetClass.getConstructor();
			contr.setAccessible(true);
			iPacket packet = contr.newInstance();
			iPacketListener listener = null;
			
			if(packet instanceof iPacketListener)
				listener = (iPacketListener) packet; // New: handle packets if
				                                     // they are their own
				                                     // listeners. No packet
				                                     // registration will be
				                                     // required.
			else
				mgr.stringClassRegistry.get(packetClass.getName()); // Otherwise
				                                                    // lookup
				                                                    // for
				                                                    // listener
				                                                    // in the
				                                                    // registry.
				                                                    // Packet
				                                                    // registration
				                                                    // will be
				                                                    // required.
				
			packet.readFromNBT(nbt.getCompoundTag("PacketData"));
			iPacket pkt = listener.onArrived(packet, ctx);
			if(pkt != null)
				return new PacketCustomNBT(pkt, nbt.getString("Channel"));
		} catch(Throwable err)
		{
			System.out.println("Can't handle packet for class " + message.nbt.getString("PacketClass"));
			err.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		nbt = ByteBufUtils.readTag(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, nbt);
	}
}