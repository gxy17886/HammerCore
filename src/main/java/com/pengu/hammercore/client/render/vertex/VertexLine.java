package com.pengu.hammercore.client.render.vertex;

public class VertexLine
{
	public final VertexPoint src, dest;
	
	public VertexLine(VertexPoint src, VertexPoint dest)
	{
		this.src = src.hashCode() > dest.hashCode() ? src : dest;
		this.dest = src.hashCode() > dest.hashCode() ? dest : src;
	}
	
	@Override
	public int hashCode()
	{
		return src.hashCode() + dest.hashCode();
	}
	
	@Override
	public String toString()
	{
		return "VertexLine{from=<" + src + ">,to=<" + dest + ">}";
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof VertexLine)) return false;
		return src.equals(((VertexLine) obj).src) && dest.equals(((VertexLine) obj).dest);
	}
	
	public double length()
	{
		return src.vec.distanceTo(dest.vec);
	}
}