package com.mrdimka.hammercore.net.pkt;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.net.packetAPI.IPacketListener;

public class PacketSwingArm implements IPacket, IPacketListener<PacketSwingArm, IPacket>
{
	public EnumHand hand;
	
	public PacketSwingArm(EnumHand hand)
	{
		this.hand = hand;
	}
	
	public PacketSwingArm()
	{
	}
	
	@Override
	public IPacket onArrived(PacketSwingArm packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			packet.run();
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void run()
	{
		Minecraft.getMinecraft().thePlayer.swingArm(hand);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("p1", hand.ordinal());
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		hand = EnumHand.values()[nbt.getInteger("p1")];
	}
}