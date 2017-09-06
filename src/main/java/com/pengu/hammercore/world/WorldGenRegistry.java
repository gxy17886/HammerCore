package com.pengu.hammercore.world;

import java.util.HashSet;
import java.util.Set;

import com.pengu.hammercore.world.gen.iWorldGenFeature;
import com.pengu.hammercore.world.gen.WorldRetroGen;

/**
 * @deprecated Use {@link WorldRetroGen} instead. Works absolutely the same but
 *             has retrogen feature
 */
public class WorldGenRegistry
{
	private static final Set<iWorldGenFeature> features = new HashSet<>();
	
	@Deprecated
	public static void registerFeature(iWorldGenFeature feature)
	{
		if(feature != null)
			features.add(feature);
	}
	
	public static Set<iWorldGenFeature> listFeatures()
	{
		return features;
	}
}