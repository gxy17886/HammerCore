package com.pengu.hammercore.api.mhb;

import com.pengu.hammercore.vec.Cuboid6;

/**
 * Registry class that allows modder to register hitboxes to a
 * {@link BlockTraceable}
 */
public interface IRayCubeRegistry
{
	public void bindBlockCube6(BlockTraceable target, Cuboid6... boxes);
	
	public void bindBlockCubeManager(BlockTraceable target, ICubeManager manager);
}