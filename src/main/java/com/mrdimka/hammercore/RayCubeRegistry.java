package com.mrdimka.hammercore;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;

import com.mrdimka.hammercore.api.mhb.BlockTraceable;
import com.mrdimka.hammercore.api.mhb.ICubeManager;
import com.mrdimka.hammercore.api.mhb.IRayCubeGetter;
import com.mrdimka.hammercore.api.mhb.IRayCubeRegistry;
import com.mrdimka.hammercore.vec.Cuboid6;

final class RayCubeRegistry implements IRayCubeRegistry, IRayCubeGetter
{
	static final RayCubeRegistry instance = new RayCubeRegistry();
	final Map<Block, Cuboid6[]> cubes = new HashMap<Block, Cuboid6[]>();
	final Map<Block, ICubeManager> mgrs = new HashMap<Block, ICubeManager>();
	
	private RayCubeRegistry()
	{
		Instance.getter = this;
	}
	
	@Override
	public void bindBlockCube6(BlockTraceable target, Cuboid6... boxes)
	{
		cubes.put(target, boxes);
	}
	
	@Override
	public Cuboid6[] getBoundCubes6(BlockTraceable target)
	{
		return mgrs.get(target) == null ? cubes.get(target) != null ? cubes.get(target) : new Cuboid6[0] : null;
	}
	
	@Override
	public void bindBlockCubeManager(BlockTraceable target, ICubeManager manager)
	{
		mgrs.put(target, manager);
	}
	
	@Override
	public ICubeManager getBoundCubeManager(BlockTraceable target)
	{
		return mgrs.get(target);
	}
	
	@Override
	public EntityPlayer func_0x834823_a()
	{
		return HammerCore.renderProxy.getClientPlayer();
	}
}