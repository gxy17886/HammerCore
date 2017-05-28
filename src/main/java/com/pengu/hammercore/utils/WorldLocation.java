package com.pengu.hammercore.utils;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.mrdimka.hammercore.net.HCNetwork;
import com.mrdimka.hammercore.net.pkt.PacketSetBiome;

public class WorldLocation
{
	private transient World world;
	private transient BlockPos pos;
	
	public WorldLocation(World world, BlockPos pos)
	{
		this.world = world;
		this.pos = pos;
	}
	
	public World getWorld()
	{
		return world;
	}
	
	public BlockPos getPos()
	{
		return pos;
	}
	
	public TileEntity getTile()
	{
		return world.isBlockLoaded(pos) ? world.getTileEntity(pos) : null;
	}
	
	public <T extends TileEntity> T getTileOfType(Class<T> tile)
	{
		return WorldUtil.cast(getTile(), tile);
	}
	
	public void setTile(TileEntity tile)
	{
		if(world.isBlockLoaded(pos))
			world.setTileEntity(pos, tile);
	}
	
	public IBlockState getState()
	{
		return world.isBlockLoaded(pos) ? world.getBlockState(pos) : Blocks.AIR.getDefaultState();
	}
	
	public Block getBlock()
	{
		return getState().getBlock();
	}
	
	public int getMeta()
	{
		return getBlock().getMetaFromState(getState());
	}
	
	public void setState(IBlockState state)
	{
		if(world.isBlockLoaded(pos))
			world.setBlockState(pos, state);
	}
	
	public Biome getBiome()
	{
		return world.isBlockLoaded(pos) ? world.getBiome(pos) : Biomes.PLAINS;
	}
	
	public void setBiome(Biome biome)
	{
		if(world.isBlockLoaded(pos))
		{
			int i = pos.getX() & 15;
			int j = pos.getZ() & 15;
			int id = j << 4 | i;
			byte[] blockBiomeArray = world.getChunkFromBlockCoords(pos).getBiomeArray();
			blockBiomeArray[id] = (byte) (Biome.getIdForBiome(biome) & 255);
			
			// update on client side
			if(!world.isRemote)
				HCNetwork.manager.sendToAllAround(new PacketSetBiome(i, j, id, blockBiomeArray[id]), getPointWithRad(296));
		}
	}
	
	public TargetPoint getPointWithRad(int radius)
	{
		return new TargetPoint(world.provider.getDimension(), pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, radius);
	}
	
	public void setMeta(int meta)
	{
		if(world.isBlockLoaded(pos))
			world.setBlockState(pos, getBlock().getStateFromMeta(meta));
	}
	
	public void setBlock(Block block)
	{
		if(world.isBlockLoaded(pos))
			world.setBlockState(pos, block.getStateFromMeta(getMeta()));
	}
	
	public WorldLocation offset(EnumFacing facing)
	{
		return offset(facing, 1);
	}
	
	public WorldLocation offset(EnumFacing facing, int steps)
	{
		return new WorldLocation(world, pos.offset(facing, steps));
	}
	
	public WorldLocation offset(int x, int y, int z)
	{
		return new WorldLocation(world, pos.add(x, y, z));
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		world = null;
		pos = null;
		super.finalize();
	}
	
	public Material getMaterial()
	{
		return getBlock().getMaterial(getState());
	}
}