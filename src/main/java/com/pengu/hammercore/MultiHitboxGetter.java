package com.pengu.hammercore;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.hammercore.api.mhb.BlockTraceable;
import com.pengu.hammercore.api.mhb.ICubeManager;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.vec.Cuboid6;

public class MultiHitboxGetter
{
	public static Cuboid6[] getCuboidsAt(World world, BlockPos pos)
	{
		BlockTraceable bt = WorldUtil.cast(world.getBlockState(pos).getBlock(), BlockTraceable.class);
		if(bt != null)
		{
			ICubeManager mgr = RayCubeRegistry.instance.getBoundCubeManager(bt);
			if(mgr != null)
				return mgr.getCuboids(world, pos, world.getBlockState(pos));
			Cuboid6[] cbs = RayCubeRegistry.instance.getBoundCubes6(bt);
			if(cbs != null)
				return cbs;
		}
		return new Cuboid6[] { new Cuboid6(world.getBlockState(pos).getBoundingBox(world, pos)) };
	}
}