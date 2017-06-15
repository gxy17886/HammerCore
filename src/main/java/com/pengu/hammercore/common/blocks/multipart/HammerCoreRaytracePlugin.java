package com.pengu.hammercore.common.blocks.multipart;

import com.pengu.hammercore.api.mhb.IRayCubeRegistry;
import com.pengu.hammercore.api.mhb.IRayRegistry;
import com.pengu.hammercore.api.mhb.RaytracePlugin;
import com.pengu.hammercore.init.ModBlocks;

@RaytracePlugin
public class HammerCoreRaytracePlugin implements IRayRegistry
{
	@Override
	public void registerCubes(IRayCubeRegistry cube)
	{
		BlockMultipart multipart = (BlockMultipart) ModBlocks.MULTIPART;
		cube.bindBlockCubeManager(multipart, multipart);
	}
}