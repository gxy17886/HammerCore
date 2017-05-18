package com.mrdimka.hammercore.common;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

/**
 * This enum used to make machines that have only four possible directions.
 **/
public enum EnumRotation implements IStringSerializable
{
	SOUTH, NORTH, EAST, WEST;
	
	public static final PropertyEnum EFACING = PropertyEnum.create("facing", EnumFacing.class);
	
	public static final PropertyEnum FACING = PropertyEnum.create("facing", EnumRotation.class);
	public static final PropertyEnum ROTATION = PropertyEnum.create("rotation", EnumRotation.class);
	
	private EnumRotation()
	{
	}
	
	public String getName()
	{
		return name().toLowerCase();
	}
	
	public String toString()
	{
		return getName();
	}
}