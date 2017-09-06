package com.pengu.hammercore.api.mhb;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.hammercore.vec.Cuboid6;

/**
 * An advanced way of getting hitboxes for {@link BlockTraceable}
 */
public interface ICubeManager
{
	public Cuboid6[] getCuboids(World world, BlockPos pos, IBlockState state);
}