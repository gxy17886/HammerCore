package com.mrdimka.hammercore.common.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.mrdimka.hammercore.HammerCore;

public class WorldUtil
{
	public static <T> T cast(Object obj, Class<T> to)
	{
		if(obj != null && to.isAssignableFrom(obj.getClass())) return (T) obj;
		return null;
	}
	
	public static World getWorld(MessageContext context, int dim)
	{
		return HammerCore.renderProxy.getWorld(context, dim);
	}
	
	public static NBTTagList saveInv(IInventory inventory)
	{
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < inventory.getSizeInventory(); ++i)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			inventory.getStackInSlot(i).writeToNBT(nbt);
			nbt.setInteger("Slot", i);
			list.appendTag(nbt);
		}
		return list;
	}
	
	public static void readInv(NBTTagList list, IInventory inv)
	{
		for(int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound nbt = list.getCompoundTagAt(i);
			inv.setInventorySlotContents(nbt.getInteger("Slot"), ItemStack.loadItemStackFromNBT(nbt));
		}
	}
	
	public static void spawnItemStack(World worldIn, double x, double y, double z, ItemStack stackIn)
    {
    	if(worldIn.isRemote) return;
    	
        EntityItem entityItem = new EntityItem(worldIn, x, y, z, stackIn);
        entityItem.motionX = 0;
        entityItem.motionZ = 0;
        worldIn.spawnEntityInWorld(entityItem);
    }
	
	public static void spawnItemStack(World worldIn, BlockPos pos, ItemStack stackIn)
    {
    	spawnItemStack(worldIn, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, stackIn);
    }
    
    public static void teleportPlayer(EntityPlayerMP mp, int dim, double x, double y, double z)
    {
    	if(!mp.worldObj.isRemote)
    	{
    		if(mp.worldObj.provider.getDimension() != dim)
    		{
    			MinecraftServer server = mp.mcServer;
    			WorldServer nev = server.worldServerForDimension(dim);
        		server.getPlayerList().transferPlayerToDimension(mp, dim, new BlankTeleporter(nev));
    		}
    		
    		mp.setPositionAndUpdate(x, y, z);
    	}
    }
    
    public static void teleportEntity(Entity ent, int dim, double x, double y, double z)
    {
    	if(!ent.worldObj.isRemote)
    	{
    		if(ent.worldObj.provider.getDimension() != dim)
    		{
    			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    			
    			WorldServer old = server.worldServerForDimension(ent.worldObj.provider.getDimension());
    			WorldServer nev = server.worldServerForDimension(dim);
    			
        		server.getPlayerList().transferEntityToWorld(ent, ent.worldObj.provider.getDimension(), old, nev, new BlankTeleporter(nev));
    		}
    		
    		ent.setPositionAndUpdate(x, y, z);
    	}
    }
    
    public static void teleportEntity(Entity ent, double x, double y, double z)
    {
    	teleportEntity(ent, ent.worldObj.provider.getDimension(), x, y, z);
    }
    
    public static void teleportEntity(Entity ent, int dim)
    {
    	teleportEntity(ent, dim, ent.posX, ent.posY, ent.posZ);
    }
}