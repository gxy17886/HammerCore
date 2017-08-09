package com.mrdimka.hammercore.common.blocks.multipart;

import com.mrdimka.hammercore.api.mhb.IRayCubeRegistry;
import com.mrdimka.hammercore.api.mhb.IRayRegistry;
import com.mrdimka.hammercore.api.mhb.RaytracePlugin;
import com.pengu.hammercore.init.BlocksHC;

@RaytracePlugin
public class HammerCoreRaytracePlugin implements IRayRegistry
{
	@Override
	public void registerCubes(IRayCubeRegistry cube)
	{
		BlockMultipart multipart = (BlockMultipart) BlocksHC.MULTIPART;
		cube.bindBlockCubeManager(multipart, multipart);
	}
}