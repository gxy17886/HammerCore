package com.mrdimka.hammercore.net.pkt;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.mrdimka.hammercore.common.utils.StrPos;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.net.packetAPI.IPacketListener;
import com.mrdimka.hammercore.tile.TileSyncable;

public class PacketSyncSyncableTile implements IPacket, IPacketListener<PacketSyncSyncableTile, IPacket>
{
	private String pos;
	private int world;
	private NBTTagCompound nbt;
	
	public PacketSyncSyncableTile()
	{
	}
	
	public PacketSyncSyncableTile(TileSyncable tile)
	{
		nbt = tile.getUpdateTag();
		pos = StrPos.toStr(tile.getPos());
		world = tile.getWorld().provider.getDimension();
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("data", this.nbt);
		nbt.setString("pos", pos);
		nbt.setInteger("dim", world);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.nbt = nbt.getCompoundTag("data");
		pos = nbt.getString("pos");
		world = nbt.getInteger("dim");
	}
	
	@Override
	public IPacket onArrived(PacketSyncSyncableTile packet, MessageContext context)
	{
		World world = WorldUtil.getWorld(context, packet.world);
		BlockPos pos = StrPos.fromStr(packet.pos);
		if(world != null && world.isAreaLoaded(pos, pos) /* prevent crashing... */)
		{
			TileSyncable sync = WorldUtil.cast(world.getTileEntity(pos), TileSyncable.class);
			if(sync != null)
				sync.handleUpdateTag(packet.nbt);
		}
		
		return null;
	}
	
}