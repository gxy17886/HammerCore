package com.pengu.hammercore.net.packetAPI.p2p;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.net.packetAPI.IPacket;
import com.pengu.hammercore.net.packetAPI.IPacketListener;

public class PacketSendTaskDim implements IPacket, IPacketListener<PacketSendTaskDim, IPacket>
{
	private NBTTagCompound task;
	private String sender;
	private int[] receivers;
	
	public PacketSendTaskDim(ITask task, int... receivers)
	{
		this.task = new NBTTagCompound();
		NBTTagCompound tag = new NBTTagCompound();
		task.writeToNBT(tag);
		this.task.setTag("Data", tag);
		this.task.setString("Class", task.getClass().getName());
		
		this.sender = "?";
		this.receivers = receivers;
	}
	
	public PacketSendTaskDim()
	{
	}
	
	@Override
	public IPacket onArrived(PacketSendTaskDim packet, MessageContext context)
	{
		if(context.side == Side.SERVER)
		{
			packet.sender = context.getServerHandler().player.getGameProfile().getName();
			for(int $receiver : packet.receivers)
				HCNetwork.manager.sendToDimension(packet, $receiver);
		}
		if(context.side == Side.CLIENT)
		{
			try
			{
				ITask task = (ITask) Class.forName(this.task.getString("Class")).newInstance();
				task.readFromNBT(this.task.getCompoundTag("Data"));
				task.execute(context);
			} catch(Throwable err)
			{
				err.printStackTrace();
			}
		}
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("Task", this.task);
		nbt.setString("Sen", sender);
		nbt.setIntArray("Rec", receivers);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.task = nbt.getCompoundTag("Task");
		sender = nbt.getString("Sen");
		receivers = nbt.getIntArray("Rec");
	}
}