package com.mrdimka.hammercore.tile;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.net.HCNetwork;
import com.mrdimka.hammercore.net.pkt.PacketSyncSyncableTile;

public abstract class TileSyncable extends TileEntity
{
	private NBTTagCompound lastSyncTag;
	
	/** Turn this to false to force this tile to sync even if it's old and new tags are equal */
	public boolean escapeSyncIfIdentical = false;
	
	@Override
	public void markDirty()
	{
		super.markDirty();
		sync();
	}
	
	public void sync()
	{
		if(escapeSyncIfIdentical)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			writeNBT(nbt);
			if(lastSyncTag != null && lastSyncTag.equals(nbt)) return; //Escape unnecessary sync if it is the same
			lastSyncTag = nbt;
		}
		
		if(world != null && !world.isRemote) //Apply sync only if server
		{
			PacketSyncSyncableTile tile = new PacketSyncSyncableTile(this);
			HCNetwork.manager.sendToAllAround(tile, getSyncPoint(512));
		}
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeNBT(nbt);
		if(this instanceof TileSyncableTickable) nbt.setInteger("ticksExisted", ((TileSyncableTickable) this).ticksExisted);
		return nbt;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		readNBT(pkt.getNbtCompound());
		if(this instanceof TileSyncableTickable) ((TileSyncableTickable) this).ticksExisted = pkt.getNbtCompound().getInteger("ticksExisted");
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		readNBT(tag);
		if(this instanceof TileSyncableTickable) ((TileSyncableTickable) this).ticksExisted = tag.getInteger("ticksExisted");
	}
	
	public TargetPoint getSyncPoint(int range)
	{
		return new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range);
	}
	
	public abstract void writeNBT(NBTTagCompound nbt);
	public abstract void readNBT(NBTTagCompound nbt);
	
	@Override
	public final NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt = super.writeToNBT(nbt);
		NBTTagCompound tag = new NBTTagCompound();
		writeNBT(tag);
		nbt.setTag("tags", tag);
		if(this instanceof TileSyncableTickable) nbt.setInteger("ticksExisted", ((TileSyncableTickable) this).ticksExisted);
		return nbt;
	}
	
	@Override
	public final void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		readNBT(nbt.getCompoundTag("tags"));
		if(this instanceof TileSyncableTickable) ((TileSyncableTickable) this).ticksExisted = nbt.getInteger("ticksExisted");
	}
	
	private IItemHandler[] itemHandlers = new SidedInvWrapper[6];
	
    protected IItemHandler createSidedHandler(EnumFacing side)
    {
    	if(this instanceof ISidedInventory) return itemHandlers[side.ordinal()] = new SidedInvWrapper((ISidedInventory) this, side);
    	if(this instanceof IInventory) return itemHandlers[side.ordinal()] = new InvWrapper((IInventory) this);
        return null;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this instanceof IInventory)
            return (T) (itemHandlers[facing.ordinal()] == null ? createSidedHandler(facing) : itemHandlers[facing.ordinal()]);
        return super.getCapability(capability, facing);
    }
    
    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this instanceof IInventory) || super.hasCapability(capability, facing);
    }
    
    public boolean atTickRate(int rate)
	{
		return (world.getTotalWorldTime() + pos.toLong()) % rate == 0;
	}
    
    public final void tryOpenGui(EntityPlayer player, World world)
    {
    	if(!world.isRemote) FMLNetworkHandler.openGui(player, HammerCore.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
    }
    
    /** NEW GUI API */
    
    public boolean hasGui()
    {
    	return false;
    }
    
	public Object getServerGuiElement(EntityPlayer player)
	{
		return null;
	}
	
	public Object getClientGuiElement(EntityPlayer player)
	{
		return null;
	}
}