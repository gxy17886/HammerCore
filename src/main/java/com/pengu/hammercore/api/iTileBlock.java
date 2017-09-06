package com.pengu.hammercore.api;

import net.minecraft.tileentity.TileEntity;

public interface ITileBlock<T extends TileEntity>
{
	public Class<T> getTileClass();
}