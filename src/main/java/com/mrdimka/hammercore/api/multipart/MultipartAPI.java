package com.mrdimka.hammercore.api.multipart;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.mrdimka.hammercore.common.blocks.multipart.TileMultipart;
import com.mrdimka.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.init.BlocksHC;

/**
 * Some useful utilities for Mutlipart API
 */
public final class MultipartAPI
{
	private MultipartAPI()
	{
	}
	
	public static TileMultipart getOrPlaceMultipart(World world, BlockPos pos)
	{
		if(world != null && pos != null && world.isBlockLoaded(pos))
		{
			TileMultipart tmp = WorldUtil.cast(world.getTileEntity(pos), TileMultipart.class);
			if(tmp == null)
				world.setBlockState(pos, BlocksHC.MULTIPART.getDefaultState(), 11);
			tmp = WorldUtil.cast(world.getTileEntity(pos), TileMultipart.class);
			if(tmp == null)
				world.setTileEntity(pos, tmp = new TileMultipart());
			return tmp;
		}
		return null;
	}
}