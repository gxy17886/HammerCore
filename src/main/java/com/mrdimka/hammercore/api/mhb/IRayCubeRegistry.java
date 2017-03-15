package com.mrdimka.hammercore.api.mhb;

import com.mrdimka.hammercore.vec.Cuboid6;

public interface IRayCubeRegistry
{
	public void bindBlockCube6(BlockTraceable target, Cuboid6... boxes);
	public void bindBlockCubeManager(BlockTraceable target, ICubeManager manager);
}