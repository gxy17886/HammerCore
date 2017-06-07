package com.pengu.hammercore.client.model.simple;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.util.EnumFacing;

import org.apache.commons.lang3.ArrayUtils;

import com.mrdimka.hammercore.math.ExpressionEvaluator;
import com.pengu.hammercore.utils.NPEUtils;

public class SimpleModelParser
{
	public static List<Opnode> toOpcodes(String model)
	{
		Opnode node = null;
		List<Opnode> nodes = new ArrayList<>();
		model = model.replaceAll("\r", "");
		String[] lines = model.split("\n");
		
		for(String s : lines)
		{
			if(s.equals("{"))
			{
				if(node == null)
					node = new Opnode(new int[0], null);
				else
					throw new RuntimeException("unterminated node " + Arrays.toString(node.opnode));
			} else if(s.equals("}"))
			{
				NPEUtils.checkNotNull(node, "node");
				nodes.add(node);
				node = null;
			} else if(s.startsWith("name "))
			{
				NPEUtils.checkNotNull(node, "node");
				node.name = s.substring(5);
			} else if(s.startsWith("texture "))
			{
				NPEUtils.checkNotNull(node, "node");
				
				String dir = s.substring(8, 10);
				int op = 0;
				if(dir.equals("x-"))
					op = EnumFacing.WEST.ordinal();
				else if(dir.equals("x+"))
					op = EnumFacing.EAST.ordinal();
				else if(dir.equals("y-"))
					op = EnumFacing.DOWN.ordinal();
				else if(dir.equals("y+"))
					op = EnumFacing.UP.ordinal();
				else if(dir.equals("z-"))
					op = EnumFacing.NORTH.ordinal();
				else if(dir.equals("z+"))
					op = EnumFacing.SOUTH.ordinal();
				else
					NPEUtils.checkNotNull(null, "unknown orientation: " + dir);
				
				node.textures[op] = s.substring(10);
			} else if(s.startsWith("textures "))
			{
				NPEUtils.checkNotNull(node, "node");
				Arrays.fill(node.textures, s.substring(9));
			} else if(s.startsWith("disable face "))
			{
				NPEUtils.checkNotNull(node, "node");
				String dir = s.substring(13, 15);
				int op = 0;
				if(dir.equals("x-"))
					op = EnumFacing.WEST.ordinal();
				else if(dir.equals("x+"))
					op = EnumFacing.EAST.ordinal();
				else if(dir.equals("y-"))
					op = EnumFacing.DOWN.ordinal();
				else if(dir.equals("y+"))
					op = EnumFacing.UP.ordinal();
				else if(dir.equals("z-"))
					op = EnumFacing.NORTH.ordinal();
				else if(dir.equals("z+"))
					op = EnumFacing.SOUTH.ordinal();
				else
					NPEUtils.checkNotNull(null, "unknown orientation: " + dir);
				
				node.opnode = ArrayUtils.addAll(node.opnode, ModelOpcodes.DFACE, op);
			} else if(s.equals("disable faces"))
			{
				NPEUtils.checkNotNull(node, "node");
				node.opnode = ArrayUtils.add(node.opnode, ModelOpcodes.DFACES);
			} else if(s.startsWith("enable face "))
			{
				NPEUtils.checkNotNull(node, "node");
				String dir = s.substring(12, 14);
				int op = 0;
				if(dir.equals("x-"))
					op = EnumFacing.WEST.ordinal();
				else if(dir.equals("x+"))
					op = EnumFacing.EAST.ordinal();
				else if(dir.equals("y-"))
					op = EnumFacing.DOWN.ordinal();
				else if(dir.equals("y+"))
					op = EnumFacing.UP.ordinal();
				else if(dir.equals("z-"))
					op = EnumFacing.NORTH.ordinal();
				else if(dir.equals("z+"))
					op = EnumFacing.SOUTH.ordinal();
				else
					NPEUtils.checkNotNull(null, "unknown orientation: " + dir);
				
				node.opnode = ArrayUtils.addAll(node.opnode, ModelOpcodes.EFACE, op);
			} else if(s.equals("enable faces"))
			{
				NPEUtils.checkNotNull(node, "node");
				node.opnode = ArrayUtils.add(node.opnode, ModelOpcodes.EFACES);
			} else if(s.startsWith("color "))
			{
				NPEUtils.checkNotNull(node, "node");
				String[] c = s.substring(6).split(" ");
				node.opnode = ArrayUtils.addAll(node.opnode, ModelOpcodes.COLOR, Integer.parseInt(c[0]), Integer.parseInt(c[1]), Integer.parseInt(c[2]), c.length == 3 ? 255 : Integer.parseInt(c[3]));
			} else if(s.startsWith("bounds "))
			{
				NPEUtils.checkNotNull(node, "node");
				String[] bds = s.substring(7).split(" ");
				node.opnode = ArrayUtils.add(node.opnode, ModelOpcodes.BOUNDS);
				node.opnode = addDouble(node.opnode, ExpressionEvaluator.evaluateDouble(bds[0]));
				node.opnode = addDouble(node.opnode, ExpressionEvaluator.evaluateDouble(bds[1]));
				node.opnode = addDouble(node.opnode, ExpressionEvaluator.evaluateDouble(bds[2]));
				node.opnode = addDouble(node.opnode, ExpressionEvaluator.evaluateDouble(bds[3]));
				node.opnode = addDouble(node.opnode, ExpressionEvaluator.evaluateDouble(bds[4]));
				node.opnode = addDouble(node.opnode, ExpressionEvaluator.evaluateDouble(bds[5]));
			} else if(s.equals("draw"))
			{
				NPEUtils.checkNotNull(node, "node");
				node.opnode = ArrayUtils.addAll(node.opnode, ModelOpcodes.DRAW);
			}
		}
		
		return nodes;
	}
	
	public static final ThreadLocal<ByteBuffer> buf = ThreadLocal.withInitial(() ->
	{
		return ByteBuffer.allocate(8);
	});
	
	public static int[] addDouble(int[] array, double d)
	{
		ByteBuffer b = buf.get();
		b.position(0);
		b.putDouble(d);
		b.position(0);
		int int1 = b.getInt();
		int int2 = b.getInt();
		return ArrayUtils.addAll(array, int1, int2);
	}
	
	public static double getDouble(int[] array, int pos)
	{
		ByteBuffer b = buf.get();
		b.position(0);
		b.putInt(array[pos]);
		b.putInt(array[pos + 1]);
		b.position(0);
		return b.getDouble();
	}
}