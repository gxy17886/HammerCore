package com.pengu.hammercore.client.render.vertex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.vec.Cuboid6;

public class ComplicatedRendering
{
	public enum EnumEdge
	{
		MIN, MAX;
		
		public EnumEdge opposite()
		{
			return this == MIN ? MAX : MIN;
		}
	}
	
	private static final ThreadLocal<Set<VertexPoint>> isEdgePoint_aps = ThreadLocal.withInitial(() ->
	{
		return new HashSet<>();
	});
	
	private static final ThreadLocal<Set<VertexPoint>> forRendering_vps = ThreadLocal.withInitial(() ->
	{
		return new HashSet<>();
	});
	
	private static final ThreadLocal<Set<VertexLine>> forRendering_lns = ThreadLocal.withInitial(() ->
	{
		return new HashSet<>();
	});
	
	/**
	 * Use {@link HashSet} as vertices argument if you don't want to duplicate
	 * same points
	 */
	public static void addVertexPoints(Collection<VertexPoint> vertices, AxisAlignedBB aabb)
	{
		vertices.add(new VertexPoint(aabb.minX, aabb.minY, aabb.minZ));
		vertices.add(new VertexPoint(aabb.minX, aabb.minY, aabb.maxZ));
		vertices.add(new VertexPoint(aabb.maxX, aabb.minY, aabb.minZ));
		vertices.add(new VertexPoint(aabb.maxX, aabb.minY, aabb.maxZ));
		vertices.add(new VertexPoint(aabb.minX, aabb.maxY, aabb.minZ));
		vertices.add(new VertexPoint(aabb.minX, aabb.maxY, aabb.maxZ));
		vertices.add(new VertexPoint(aabb.maxX, aabb.maxY, aabb.minZ));
		vertices.add(new VertexPoint(aabb.maxX, aabb.maxY, aabb.maxZ));
	}
	
	/**
	 * Use {@link HashSet} as vertices argument if you don't want to duplicate
	 * same points
	 */
	public static void addVertexPoints(Collection<VertexPoint> vertices, Cuboid6 cuboid)
	{
		vertices.add(new VertexPoint(cuboid.min.x, cuboid.min.y, cuboid.min.z));
		vertices.add(new VertexPoint(cuboid.min.x, cuboid.min.y, cuboid.max.z));
		vertices.add(new VertexPoint(cuboid.max.x, cuboid.min.y, cuboid.min.z));
		vertices.add(new VertexPoint(cuboid.max.x, cuboid.min.y, cuboid.max.z));
		vertices.add(new VertexPoint(cuboid.min.x, cuboid.max.y, cuboid.min.z));
		vertices.add(new VertexPoint(cuboid.min.x, cuboid.max.y, cuboid.max.z));
		vertices.add(new VertexPoint(cuboid.max.x, cuboid.max.y, cuboid.min.z));
		vertices.add(new VertexPoint(cuboid.max.x, cuboid.max.y, cuboid.max.z));
	}
	
	public static Set<VertexPoint> forRendering(AxisAlignedBB... aabbs)
	{
		Set<VertexPoint> points = forRendering_vps.get();
		points.clear();
		for(AxisAlignedBB aabb : aabbs)
			addVertexPoints(points, aabb);
		return points;
	}
	
	public static Set<VertexLine> forRendering(Collection<VertexPoint> points)
	{
		Set<VertexLine> lns = forRendering_lns.get();
		lns.clear();
		makeLines(points, lns);
		return lns;
	}
	
	public static Set<VertexPoint> forRendering(Cuboid6... cuboids)
	{
		Set<VertexPoint> points = forRendering_vps.get();
		points.clear();
		for(Cuboid6 aabb : cuboids)
			addVertexPoints(points, aabb);
		return points;
	}
	
	public static void addVertexPointsOnSameAxis(Axis axis, double value1, double value2, Collection<VertexPoint> src, Collection<VertexPoint> dst)
	{
		for(VertexPoint p : src)
			if(p.getPos1(axis) == value1 && p.getPos2(axis) == value2)
				dst.add(p);
	}
	
	public static void renderLines(double x, double y, double z, Collection<VertexLine> lines)
	{
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.disableTexture2D();
		GlStateManager.depthMask(false);
		
		GL11.glPushMatrix();
		
		GL11.glTranslated(x, y, z);
		
		GL11.glBegin(GL11.GL_LINES);
		
		for(VertexLine l : lines)
		{
			GL11.glVertex3d(l.src.vec.xCoord, l.src.vec.yCoord, l.src.vec.zCoord);
			GL11.glVertex3d(l.dest.vec.xCoord, l.dest.vec.yCoord, l.dest.vec.zCoord);
		}
		
		GL11.glEnd();
		
		GL11.glPopMatrix();
		
		GlStateManager.depthMask(true);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}
	
	public static EnumEdge isEdgePoint(Collection<VertexPoint> src, VertexPoint point, Axis axis)
	{
		if(!src.contains(point))
			return null;
		
		double min = Double.POSITIVE_INFINITY;
		VertexPoint mnp = null;
		
		double max = Double.NEGATIVE_INFINITY;
		VertexPoint mxp = null;
		
		Set<VertexPoint> aps = isEdgePoint_aps.get();
		aps.clear();
		
		addVertexPointsOnSameAxis(axis, point.getPos1(axis), point.getPos2(axis), src, aps);
		
		for(VertexPoint vp : aps)
		{
			double val = vp.getPos(axis);
			
			if(val <= min)
				mnp = vp;
			min = Math.min(min, val);
			
			if(val >= max)
				mxp = vp;
			max = Math.max(max, val);
		}
		
		return mnp == point ? EnumEdge.MIN : mxp == point ? EnumEdge.MAX : null;
	}
	
	public static VertexPoint getEdgePoint(Collection<VertexPoint> src, Axis axis, double value1, double value2, EnumEdge edge)
	{
		double min = Double.POSITIVE_INFINITY;
		VertexPoint mnp = null;
		
		double max = Double.NEGATIVE_INFINITY;
		VertexPoint mxp = null;
		
		Set<VertexPoint> aps = isEdgePoint_aps.get();
		aps.clear();
		
		addVertexPointsOnSameAxis(axis, value1, value2, src, aps);
		
		for(VertexPoint vp : aps)
		{
			double val = vp.getPos(axis);
			
			if(val <= min)
				mnp = vp;
			min = Math.min(min, val);
			
			if(val >= max)
				mxp = vp;
			max = Math.max(max, val);
		}
		
		return edge == EnumEdge.MIN ? mnp : mxp;
	}
	
	public static void makeLines(Collection<VertexPoint> points, Collection<VertexLine> lns_)
	{
		List<VertexLine> lns = new ArrayList<>(lns_);
		for(VertexPoint p1 : points)
			for(VertexPoint p2 : points)
				if(p1 != p2)
				{
					Axis ax = p1.onSameAxisWith(p2);
					if(ax != null && shouldMakeLine(p1, p2, points))
					{
						VertexLine ln = new VertexLine(p1, p2);
						if(ln.length() > 0)
							lns.add(ln);
					}
				}
		handleSimilars(lns);
		lns_.clear();
		lns_.addAll(lns);
	}
	
	private static void handleSimilars(List<VertexLine> lines)
	{
		LinkedHashSet<VertexLine> ls = new LinkedHashSet<VertexLine>(lines);
		LinkedHashSet<VertexLine> delta = new LinkedHashSet<VertexLine>();
		
		for(VertexLine l : lines)
			if(!ls.contains(l))
				delta.add(l);
		
		ls.removeAll(delta);
		
		lines.clear();
		lines.addAll(ls);
		
		// boolean s = false;
		//
		// do
		// {
		// s = false;
		//
		// for(int i = 0; i < lines.size(); ++i)
		// {
		// int in = indexOf(lines.get(i), lines, i + 1);
		// if(in != -1)
		// {
		// lines.remove(in);
		// s = true;
		// break;
		// }
		// }
		// } while(s);
	}
	
	// private static <T> boolean contains(T t, List<T> list)
	// {
	// for(int i = 0; i < list.size(); ++i)
	// if(list.get(i).hashCode() == t.hashCode())
	// return true;
	// return false;
	// }
	
	private static <T> int indexOf(T obj, List<T> list, int startPos)
	{
		if(obj == null)
			return -1;
		for(int i = startPos; i < list.size(); ++i)
			if(obj.hashCode() == list.get(i).hashCode())
				return i;
		return -1;
	}
	
	private static <T> int count(T t, List<T> list, int startPos)
	{
		int c = 0;
		for(int i = startPos; i < list.size(); ++i)
			if(t.hashCode() == list.get(i).hashCode())
				c++;
		return c;
	}
	
	public static boolean shouldMakeLine(VertexPoint p1, VertexPoint p2, Collection<VertexPoint> points)
	{
		if(!points.contains(p2) || !points.contains(p1))
			return false;
		Axis ax = p1.onSameAxisWith(p2);
		if(ax == null) return false;
		EnumEdge e = isEdgePoint(points, p1, ax);
		if(e != null && e.opposite() == isEdgePoint(points, p2, ax) && hasNothingBetween(p1, p2, points))
			return true;
		return false;
	}
	
	public static boolean isBetween(VertexPoint p, VertexPoint p1, VertexPoint p2)
	{
		if(p.onSameAxisWith(p1) == p.onSameAxisWith(p2))
		{
			Axis a = p.onSameAxisWith(p1);
			if(a == null)
				return false;
			
//			if(p1.vec.distanceTo(p.vec) + p2.vec.distanceTo(p.vec) == p1.vec.distanceTo(p2.vec))
//				return true;
			
			if(p1.vec.distanceTo(p.vec) + p2.vec.distanceTo(p.vec) == p1.vec.distanceTo(p2.vec))
				return true;
		}
		
		return false;
	}
	
	public static boolean hasNothingBetween(VertexPoint p1, VertexPoint p2, Collection<VertexPoint> points)
	{
		for(VertexPoint p : points)
			if(isBetween(p, p1, p2))
				return false;
		return true;
	}
}