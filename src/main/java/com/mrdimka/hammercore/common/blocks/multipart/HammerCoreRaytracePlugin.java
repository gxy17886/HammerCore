package com.mrdimka.hammercore.common.blocks.multipart;

import com.mrdimka.hammercore.api.mhb.IRayCubeRegistry;
import com.mrdimka.hammercore.api.mhb.IRayRegistry;
import com.mrdimka.hammercore.api.mhb.RaytracePlugin;
import com.mrdimka.hammercore.init.ModBlocks;

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