package com.mrdimka.hammercore.net.pkt;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.net.HCNetwork;
import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.net.packetAPI.IPacketListener;
import com.mrdimka.hammercore.tile.TileSyncable;
import com.pengu.hammercore.net.utils.NetPropertyAbstract;

public class PacketSetProperty implements IPacket, IPacketListener<PacketSetProperty, IPacket>
{
	protected NBTTagCompound nbt;
	
	@SideOnly(Side.CLIENT)
	public static void toServer(TileSyncable tile, NetPropertyAbstract abs)
	{
		HCNetwork.manager.sendToServer(new PacketSetProperty(tile, abs));
	}
	
	public PacketSetProperty(TileSyncable tile, NetPropertyAbstract property)
	{
		nbt = new NBTTagCompound();
		nbt.setLong("Pos", tile.getPos().toLong());
		nbt.setInteger("Dim", tile.getWorld().provider.getDimension());
		nbt.setTag("Data", property.writeToNBT(new NBTTagCompound()));
		nbt.setInteger("Id", property.getId());
	}
	
	public PacketSetProperty()
	{
	}
	
	@Override
	public IPacket onArrived(PacketSetProperty packet, MessageContext context)
	{
		if(context.side == Side.SERVER)
		{
			NBTTagCompound nbt = packet.nbt;
			
			int dim = nbt.getInteger("Dim");
			BlockPos pos = BlockPos.fromLong(nbt.getLong("Pos"));
			NBTTagCompound prop = nbt.getCompoundTag("Data");
			int id = nbt.getInteger("Id");
			
			MinecraftServer server = context.getServerHandler().playerEntity.mcServer;
			WorldServer world = server.worldServerForDimension(dim);
			
			if(world != null && world.isBlockLoaded(pos) && world.getTileEntity(pos) instanceof TileSyncable)
				((TileSyncable) world.getTileEntity(pos)).load(id, prop);
		} else
			HammerCore.LOG.warn("Attempted to run PacketSetProperty on client. This is not going to work! Use TileSyncable.sync() instead!");
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("Data", this.nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.nbt = nbt.getCompoundTag("Data");
	}
}