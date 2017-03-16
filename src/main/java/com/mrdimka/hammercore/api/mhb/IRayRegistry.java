package com.mrdimka.hammercore.api.mhb;

/**
 * A basic plugin interface. registerCubes is called on server start.
 */
public interface IRayRegistry
{
	public void registerCubes(IRayCubeRegistry cube);
}