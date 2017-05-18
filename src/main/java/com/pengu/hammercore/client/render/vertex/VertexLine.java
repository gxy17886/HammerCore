package com.pengu.hammercore.client.render.vertex;

public class VertexLine
{
	public final VertexPoint src, dest;
	
	public VertexLine(VertexPoint src, VertexPoint dest)
	{
		this.src = src;
		this.dest = dest;
	}
	
	@Override
	public int hashCode()
	{
		return src.hashCode() * dest.hashCode() & dest.hashCode() * 3;
	}
	
	@Override
	public String toString()
	{
		return "VertexLine{from=<" + src + ">,to=<" + dest + ">}";
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof VertexLine))
			return false;
		return (src.equals(((VertexLine) obj).src) && dest.equals(((VertexLine) obj).dest)) || (dest.equals(((VertexLine) obj).src) && src.equals(((VertexLine) obj).dest));
	}
	
	public double length()
	{
		return src.vec.distanceTo(dest.vec);
	}
}