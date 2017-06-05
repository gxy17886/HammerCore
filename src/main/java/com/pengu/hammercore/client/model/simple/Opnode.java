package com.pengu.hammercore.client.model.simple;

import java.io.Serializable;
import java.util.Arrays;

import net.minecraft.util.EnumFacing;

import com.pengu.hammercore.utils.IndexedMap;

public class Opnode implements Serializable
{
	public int[] opnode;
	public String[] textures = new String[6];
	public String name;
	
	public Opnode(int[] opnode, String tex)
	{
		Arrays.fill(textures, tex);
		this.opnode = opnode;
	}
	
	public String toString()
	{
		return stringify();
	}
	
	public void verify()
	{
		for(int i = 0; i < opnode.length; ++i)
		{
			int code = opnode[i];
			
			if(code == ModelOpcodes.DFACE)
			{
				i++;
				int face = opnode[i];
				String sface = (face < 2 ? "y" : face < 4 ? "z" : "x") + (face % 2 == 0 ? "-" : "+");
				continue;
			} else
			
			if(code == ModelOpcodes.EFACE)
			{
				i++;
				int face = opnode[i];
				String sface = (face < 2 ? "y" : face < 4 ? "z" : "x") + (face % 2 == 0 ? "-" : "+");
				continue;
			} else
			
			if(code == ModelOpcodes.COLOR)
			{
				i++;
				int r = opnode[i];
				i++;
				int g = opnode[i];
				i++;
				int b = opnode[i];
				i++;
				int a = opnode[i];
				continue;
			} else
			
			if(code == ModelOpcodes.BOUNDS)
			{
				double m1 = SimpleModelParser.getDouble(opnode, i + 1);
				double m2 = SimpleModelParser.getDouble(opnode, i + 3);
				double m3 = SimpleModelParser.getDouble(opnode, i + 5);
				double m4 = SimpleModelParser.getDouble(opnode, i + 7);
				double m5 = SimpleModelParser.getDouble(opnode, i + 9);
				double m6 = SimpleModelParser.getDouble(opnode, i + 11);
				i += 12;
				continue;
			} else if(code == ModelOpcodes.EFACES)
				;
			else if(code == ModelOpcodes.DFACES)
				;
			else if(code == ModelOpcodes.DRAW)
				;
		}
	}
	
	public String stringify()
	{
		String s = "{\n";
		s += "name " + name + "\n";
		for(int face = 0; face < 6; ++face)
		{
			String sface = (face < 2 ? "y" : face < 4 ? "z" : "x") + (face % 2 == 0 ? "-" : "+");
			s += "texture " + sface + " " + textures[face] + "\n";
		}
		
		for(int i = 0; i < opnode.length; ++i)
		{
			int code = opnode[i];
			
			if(code == ModelOpcodes.DFACE)
			{
				i++;
				int face = opnode[i];
				String sface = (face < 2 ? "y" : face < 4 ? "z" : "x") + (face % 2 == 0 ? "-" : "+");
				s += "disable face " + sface + "\n";
				continue;
			} else
			
			if(code == ModelOpcodes.EFACE)
			{
				i++;
				int face = opnode[i];
				String sface = (face < 2 ? "y" : face < 4 ? "z" : "x") + (face % 2 == 0 ? "-" : "+");
				s += "enable face " + sface + "\n";
				continue;
			} else
			
			if(code == ModelOpcodes.COLOR)
			{
				i++;
				int r = opnode[i];
				i++;
				int g = opnode[i];
				i++;
				int b = opnode[i];
				i++;
				int a = opnode[i];
				s += "color " + r + " " + g + " " + b + (a != 255 ? " " + a : "") + "\n";
				continue;
			} else
			
			if(code == ModelOpcodes.BOUNDS)
			{
				double m1 = SimpleModelParser.getDouble(opnode, i + 1);
				double m2 = SimpleModelParser.getDouble(opnode, i + 3);
				double m3 = SimpleModelParser.getDouble(opnode, i + 5);
				double m4 = SimpleModelParser.getDouble(opnode, i + 7);
				double m5 = SimpleModelParser.getDouble(opnode, i + 9);
				double m6 = SimpleModelParser.getDouble(opnode, i + 11);
				s += "bounds " + m1 + " " + m2 + " " + m3 + " " + m4 + " " + m5 + " " + m6 + "\n";
				i += 12;
				continue;
			} else if(code == ModelOpcodes.EFACES)
				s += "enable faces\n";
			else if(code == ModelOpcodes.DFACES)
				s += "disable faces\n";
			else if(code == ModelOpcodes.DRAW)
				s += "draw\n";
		}
		
		if(s.endsWith("\n"))
			s = s.substring(0, s.length() - 1);
		
		return s + "\n}";
	}
}