package com.mrdimka.hammercore.api.multipart;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.api.handlers.IHandlerProvider;
import com.mrdimka.hammercore.common.blocks.multipart.TileMultipart;
import com.mrdimka.hammercore.common.utils.WorldUtil;

/**
 * The main part of {@link MultipartAPI}
 */
public abstract class MultipartSignature
{
	protected TileMultipart owner;
	protected World world;
	protected BlockPos pos;
	protected IBlockState state;
	
	public final void setOwner(TileMultipart owner)
	{
		this.owner = owner;
	}
	
	public final TileMultipart getOwner()
	{
		return owner;
	}
	
	public final void requestSync()
	{
		if(owner != null) owner.sync();
	}
	
	public final IBlockState getState()
	{
		return state;
	}
	
	public final void setState(IBlockState state)
	{
		this.state = state;
	}
	
	public final void setWorld(World worldIn)
	{
		world = worldIn;
	}
	
	public final void setPos(BlockPos posIn)
	{
		pos = posIn;
	}
	
	public IHandlerProvider getProvider(EnumFacing toFace)
	{
		IHandlerProvider provider = owner;
		if(world != null && world.isBlockLoaded(pos.offset(toFace)))
		{
			provider = WorldUtil.cast(world, IHandlerProvider.class);
		}
		return provider != null ? provider : owner;
	}
	
	/**
	 * Determines if multi-part signature can be placed onto a tile.
	 */
	public boolean canPlaceInto(TileMultipart tmp)
	{
		return tmp != null && tmp.canPlace_def(this);
	}
	
	public boolean onSignatureActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return false;
	}
	
	public int getLightLevel()
	{
		return 0;
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		
	}
	
	public abstract AxisAlignedBB getBoundingBox();
	
	public static MultipartSignature createAndLoadSignature(NBTTagCompound nbt)
	{
		try
		{
			MultipartSignature signature = (MultipartSignature) Class.forName(nbt.getString("class")).newInstance();
			signature.readSignature(nbt);
			return signature;
		} catch(Throwable err) { HammerCore.LOG.error("Failed to load signature for " + nbt.getString("class") + "! This is a bug!"); err.printStackTrace(); }
		return null;
	}
	
	public final void writeSignature(NBTTagCompound nbt)
	{
		nbt.setString("class", getClass().getName());
		if(state != null)
		{
			nbt.setString("block", state.getBlock().getRegistryName().toString());
			nbt.setInteger("meta", state.getBlock().getMetaFromState(state));
		}
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		nbt.setTag("nbt", tag);
	}
	
	public final void readSignature(NBTTagCompound nbt)
	{
		if(nbt.hasKey("block", NBT.TAG_STRING) && nbt.hasKey("meta", NBT.TAG_INT)) state = GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(nbt.getString("block"))).getStateFromMeta(nbt.getInteger("meta"));
		pos = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
		readFromNBT(nbt.getCompoundTag("nbt"));
	}
	
	/** Drop all things in here! */
	public void onRemoved(boolean spawnDrop)
	{
		if(spawnDrop && !world.isRemote && state != null)
			for(ItemStack stack : state.getBlock().getDrops(world, pos, state, 0))
				WorldUtil.spawnItemStack(world, pos, stack);
	}
}