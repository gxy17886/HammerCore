package com.mrdimka.hammercore.api.mhb;

import net.minecraft.entity.player.EntityPlayer;

import com.mrdimka.hammercore.vec.Cuboid6;

/**
 * Registry class that allows modder to get hitboxes for a {@link BlockTraceable}
 */
public interface IRayCubeGetter
{
	public Cuboid6[] getBoundCubes6(BlockTraceable target);
	public ICubeManager getBoundCubeManager(BlockTraceable target);
	public EntityPlayer func_0x834823_a();
	public static class Instance { public static IRayCubeGetter getter; };
}