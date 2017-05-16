package com.mrdimka.hammercore.proxy;

import net.minecraftforge.fml.relauncher.Side;

public class PipelineProxy_Client extends PipelineProxy_Common
{
	@Override
	public Side getGameSide()
	{
		return Side.CLIENT;
	}
}