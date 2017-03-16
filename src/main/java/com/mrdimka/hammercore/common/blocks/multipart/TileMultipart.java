package com.mrdimka.hammercore.common.blocks.multipart;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

import com.mrdimka.hammercore.api.handlers.IHandlerProvider;
import com.mrdimka.hammercore.api.handlers.ITileHandler;
import com.mrdimka.hammercore.api.multipart.IMultipartHandlerProvider;
import com.mrdimka.hammercore.api.multipart.MultipartSignature;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.tile.TileSyncableTickable;
import com.mrdimka.hammercore.vec.Cuboid6;

public class TileMultipart extends TileSyncableTickable implements IHandlerProvider
{
	private final Set<MultipartSignature> signatures = new HashSet<>();
	private Cuboid6[] lastBaked = null;
	
	private boolean hasSyncedOnce = false;
	
	private Set<MultipartSignature> renderSignatures = new HashSet<>();
	
	@Override
	public void tick()
	{
		MultipartSignature toRemove = null;
		for(MultipartSignature signature : renderSignatures)
		{
			if(signature.getOwner() != this)
			{
				toRemove = signature;
				continue;
			}
			
			signature.setWorld(world);
			signature.setPos(pos);
			
			if(signature instanceof ITickable)
				((ITickable) signature).update();
		}
		
		//Force-remove is we don't own this signature.
		removeMultipart(toRemove, false);
		
		if(signatures.isEmpty() && !world.isRemote)
			world.setBlockToAir(pos);
		
		if(!hasSyncedOnce) sync();
	}
	
	public Set<MultipartSignature> signatures()
	{
		return renderSignatures;
	}
	
	public boolean onBoxActivated(int boxID, Cuboid6 box, World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		for(MultipartSignature s : renderSignatures)
			if(s != null && box != null && s.getBoundingBox() != null && s.getBoundingBox().intersectsWith(box.aabb()))
				return s.onSignatureActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
		return false;
	}
	
	public int getLightLevel()
	{
		int max = 0;
		for(MultipartSignature s : renderSignatures) max = Math.max(max, s.getLightLevel());
		return max;
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		NBTTagList list = nbt.getTagList("signature", NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound tag = list.getCompoundTagAt(i);
			addMultipart(MultipartSignature.createAndLoadSignature(tag));
		}
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for(MultipartSignature s : renderSignatures)
		{
			NBTTagCompound tag = new NBTTagCompound();
			s.writeSignature(tag);
			list.appendTag(tag);
		}
		nbt.setTag("signature", list);
	}
	
	public int getNextSignatureIndex()
	{
		return renderSignatures.size();
	}
	
	public boolean canPlace_def(MultipartSignature signature)
	{
		AxisAlignedBB aabb = signature.getBoundingBox();
		for(MultipartSignature s : renderSignatures)
			if(s.getBoundingBox() != null && s.getBoundingBox().intersectsWith(aabb))
				return false;
		return true;
	}
	
	public boolean canPlace(MultipartSignature signature)
	{
		return signature.canPlaceInto(this);
	}
	
	public boolean addMultipart(MultipartSignature signature)
	{
		if(!canPlace(signature)) return false;
		signature.setPos(pos);
		signature.setWorld(world);
		signature.setOwner(this);
		signatures.add(signature);
		renderSignatures = new HashSet<>(signatures);
		lastBaked = null;
		if(world != null && !world.isRemote) sync();
		return true;
	}
	
	public void removeMultipart(MultipartSignature signature, boolean spawnDrop)
	{
		if(!signatures.contains(signature)) return;
		signature.onRemoved(spawnDrop);
		signatures.remove(signature);
		renderSignatures = new HashSet<>(signatures);
		signature.setOwner(null);
		lastBaked = null;
		if(world != null && !world.isRemote) sync();
	}
	
	public MultipartSignature getSignature(Vec3d pos)
	{
		for(MultipartSignature s : renderSignatures)
			if(s.getBoundingBox() != null && s.getBoundingBox().intersects(pos.addVector(-.0001, -.0001, -.0001), pos.addVector(.0001, .0001, .0001)))
				return s;
		return null;
	}
	
	public Cuboid6[] getCuboids()
	{
		if(lastBaked == null) lastBaked = bakeCuboids();
		return lastBaked;
	}
	
	public Cuboid6[] bakeCuboids()
	{
		List<Cuboid6> cubs = new ArrayList<>();
		for(MultipartSignature signature : renderSignatures)
			cubs.add(new Cuboid6(signature.getBoundingBox()));
		return cubs.toArray(new Cuboid6[0]);
	}
	
	@Override
	public <T extends ITileHandler> T getHandler(EnumFacing facing, Class<T> handler, Object... params)
	{
		for(MultipartSignature signature : renderSignatures)
		{
			IHandlerProvider provider = WorldUtil.cast(signature, IHandlerProvider.class);
			if(provider != null)
			{
				T h = provider.getHandler(facing, handler, params);
				if(handler != null) return h;
			}
		}
		
		return null;
	}
	
	@Override
	public <T extends ITileHandler> boolean hasHandler(EnumFacing facing, Class<T> handler, Object... params)
	{
		for(MultipartSignature signature : renderSignatures)
		{
			IHandlerProvider provider = WorldUtil.cast(signature, IHandlerProvider.class);
			if(provider != null && provider.hasHandler(facing, handler, params)) return true;
		}
		
		return false;
	}
}