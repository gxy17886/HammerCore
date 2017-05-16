package com.pengu.hammercore.client.render.vertex;

import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.Vec3d;

public class VertexPoint
{
	public Vec3d vec;
	
	public VertexPoint(double x, double y, double z)
	{
		vec = new Vec3d(x, y, z);
	}
	
	public VertexPoint(Vec3d fromVector)
	{
		vec = fromVector;
	}
	
	public double getPos(Axis axis)
	{
		return axis == Axis.X ? vec.xCoord : axis == Axis.Y ? vec.yCoord : vec.zCoord;
	}
	
	public double getPos1(Axis axis)
	{
		return axis == Axis.X ? vec.yCoord : axis == Axis.Y ? vec.zCoord : vec.xCoord;
	}
	
	public double getPos2(Axis axis)
	{
		return axis == Axis.X ? vec.zCoord : axis == Axis.Y ? vec.xCoord : vec.yCoord;
	}
	
	public Axis onSameAxisWith(VertexPoint point)
	{
		for(Axis ax : Axis.values())
			if(getPos1(ax) == point.getPos1(ax) && getPos2(ax) == point.getPos2(ax))
				return ax;
		return null;
	}
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
	@Override
	public String toString()
	{
		return "VertexPoint{" + vec.toString() + "}";
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof VertexPoint)) return false;
	    return ((VertexPoint) obj).vec.equals(vec);
	}
}