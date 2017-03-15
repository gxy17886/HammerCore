package com.mrdimka.hammercore.api.mhb;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mrdimka.hammercore.vec.Cuboid6;

public interface ICubeManager
{
	public Cuboid6[] getCuboids(World world, BlockPos pos, IBlockState state);
}