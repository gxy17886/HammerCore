package com.pengu.hammercore.world;

import java.util.HashSet;
import java.util.Set;

import com.pengu.hammercore.world.gen.IWorldGenFeature;
import com.pengu.hammercore.world.gen.WorldRetroGen;

/**
 * @deprecated Use {@link WorldRetroGen} instead. Works absolutely the same but
 *             has retrogen feature
 */
public class WorldGenRegistry
{
	private static final Set<IWorldGenFeature> features = new HashSet<>();
	
	@Deprecated
	public static void registerFeature(IWorldGenFeature feature)
	{
		if(feature != null)
			features.add(feature);
	}
	
	public static Set<IWorldGenFeature> listFeatures()
	{
		return features;
	}
}