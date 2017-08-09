package com.mrdimka.hammercore.net.pkt;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.net.packetAPI.IPacketListener;

/**
 * If sent to server, creates an {@link EntityItem} at sender's position
 */
public class PacketDropItem implements IPacket, IPacketListener<PacketDropItem, IPacket>
{
	ItemStack stack;
	NBTTagCompound ei;
	
	public PacketDropItem(ItemStack stack)
	{
		this.stack = stack;
	}
	
	public PacketDropItem(EntityItem ei)
	{
		this.stack = null;
		this.ei = ei.writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		if(stack != null)
			nbt.setTag("i", stack.writeToNBT(new NBTTagCompound()));
		if(ei != null)
			nbt.setTag("e", ei);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		stack = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("i"));
		if(nbt.hasKey("e"))
			ei = nbt.getCompoundTag("e");
	}
	
	@Override
	public IPacket onArrived(PacketDropItem packet, MessageContext context)
	{
		if(context.side == Side.SERVER)
		{
			EntityPlayerMP mp = context.getServerHandler().playerEntity;
			EntityItem ei = new EntityItem(mp.worldObj, mp.posX, mp.posY, mp.posZ, packet.stack);
			ei.setNoPickupDelay();
			if(packet.ei != null)
				ei.readFromNBT(packet.ei);
			mp.worldObj.spawnEntityInWorld(ei);
		}
		return null;
	}
}