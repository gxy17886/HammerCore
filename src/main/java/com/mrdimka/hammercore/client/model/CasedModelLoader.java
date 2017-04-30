package com.mrdimka.hammercore.client.model;

import java.util.Arrays;
import java.util.Set;

import net.minecraft.client.model.ModelRenderer;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.client.model.SimpleModelLoader.ModelLoadingException;
import com.mrdimka.hammercore.client.model.SimpleModelLoader.ModelLoadingExceptionMessage;
import com.mrdimka.hammercore.client.model.file.ModelCube;
import com.mrdimka.hammercore.client.model.file.ModelPart;

public class CasedModelLoader
{
	/**
	 * Converts string with model data to a model
	 **/
	public static CasedModel convert(String raw, boolean ignoreIssues) throws ModelLoadingException
	{
		raw = raw.replaceAll("\r\n", "\n");
		CasedModel model = new CasedModel();
		String lines[] = raw.split("\n");
		
		for(int i = 0; i < lines.length; ++i)
			if(lines[i].contains("#"))
				lines[i] = lines[i].substring(0, lines[i].indexOf("#"));
		
		ModelPart current = null;
		String condition = "";
		
		int ln = 0;
		for(String line : lines)
		{
			String l = line.toLowerCase().replaceAll("\n", "").replaceAll("\r", "");
			
			ln++;
			try
			{
				if(l.isEmpty()) continue;
				if(l.startsWith("texturewidth ")) model.textureWidth = Integer.parseInt(l.substring("texturewidth ".length()));
				if(l.startsWith("textureheight ")) model.textureHeight = Integer.parseInt(l.substring("textureheight ".length()));
				
				if(l.equals("start"))
				{
					if(current != null) throw new ModelLoadingException("Unexpected start: previous part not ended!");
					current = new ModelPart();
					condition = "";
				}
				
				if(l.startsWith("conditions ")) condition = l.substring("conditions ".length());
				
				if(l.startsWith("name "))
				{
					if(current == null) throw new ModelLoadingExceptionMessage("Unexpected box: Cannot set name of nothing!");
					current.name = l.substring("name ".length());
				}
				
				if(l.startsWith("addbox "))
				{
					String l0 = l.substring("addbox ".length());
					String[] args = l0.trim().split(" ");
					if(current == null) throw new ModelLoadingExceptionMessage("Unexpected box: Cannot add box to nothing!");
					float[] xyz = parseFloats(3, args);
					int[] size = parseInts(3, Arrays.copyOfRange(args, 3, args.length));
					current.addBox(xyz[0], xyz[1], xyz[2], size[0], size[1], size[2]);
				}
				
				if(l.startsWith("rotationpoint "))
				{
					String l0 = l.substring("rotationpoint ".length());
					String[] args = l0.trim().split(" ");
					if(current == null) throw new ModelLoadingExceptionMessage("Unexpected box: Cannot set rotation point to nothing!");
					float[] xyz = parseFloats(3, args);
					current.setRotationPoint(xyz[0], xyz[1], xyz[2]);
				}
				
				if(l.startsWith("texturesize "))
				{
					String l0 = l.substring("texturesize ".length());
					String[] args = l0.trim().split(" ");
					if(current == null) throw new ModelLoadingExceptionMessage("Unexpected box: Cannot set texture size to nothing!");
					float[] xy = parseFloats(2, args);
					current.textureWidth = xy[0];
					current.textureHeight = xy[1];
				}
				
				if(l.startsWith("mirror "))
				{
					String l0 = l.substring("mirror ".length());
					String[] args = l0.trim().split(" ");
					if(current == null) throw new ModelLoadingExceptionMessage("Unexpected box: Cannot set mirror to nothing!");
					boolean[] mirror = parseBooleans(1, args);
					current.mirror = mirror[0];
				}
				
				if(l.equals("end"))
				{
					if(current == null) throw new ModelLoadingException("Unexpected end: Cannot end nothing!");
					model.addBox(current, condition);
					condition = "";
					current = null;
				}
			}
			catch(Throwable err)
			{
				String msg = "Can't parse model line #" + ln + ": " + err.getClass().getName() + " > " + err.getMessage();
				
				if(err instanceof ModelLoadingException) msg = "Can't parse model line #" + ln + ": " + err.getMessage();
				
				if(ignoreIssues) System.err.println(msg);
				else throw new ModelLoadingException(msg);
			}
		}
		
		return model;
	}
	
	/**
	 * Converts model into string, that can be stored in a file.
	 **/
	public static String convert(CasedModel model)
	{
		String parsed = "";
		
		parsed += ln("# This model was created by HammerCore @VERSION@ (author: Pengu).");
		parsed += ln();
		parsed += ln("# This part sets texture size for renderer.");
		parsed += ln("TextureWidth " + model.textureWidth);
		parsed += ln("TextureHeight " + model.textureHeight);
		for(ModelPart part : model.PREDICATES.keySet())
		{
			Set<ModelCube> boxes = part.boxes;
			if(boxes == null || boxes.isEmpty()) continue;
			
			parsed += ln();
			parsed += ln("# New Model Part Definition" + (part.name != null && !part.name.isEmpty() ? ", Part Name: " + part.name : "."));
			parsed += ln("start");
			parsed += ln("name " + part.name);
			parsed += ln("conditions " + model.PREDICATES.get(part));
			parsed += ln("rotationPoint " + part.rotationPointX + " " + part.rotationPointY + " " + part.rotationPointZ);
			parsed += ln("textureSize " + part.textureWidth + " " + part.textureHeight);
			parsed += ln("mirror " + part.mirror);
			
			for(ModelCube box : boxes) parsed += ln("addBox " + box.posX1 + " " + box.posY1 + " " + box.posZ1 + " " + ((int) (box.posX2 - box.posX1)) + " " + ((int) (box.posY2 - box.posY1)) + " " + ((int) (box.posZ2 - box.posZ1)));
			
			parsed += ln("end");
		}
		
		return parsed;
	}
	
	private static String ln(String ln)
	{
		return ln + ln();
	}
	
	private static String ln()
	{
		return "\n";
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	private static boolean[] parseBooleans(int needed, String... strings)
	{
		boolean[] booleans = new boolean[needed];
		for(int i = 0; i < Math.min(needed, strings.length); ++i)
			try { booleans[i] = Boolean.parseBoolean(strings[i]); } catch(Throwable err) {}
		return booleans;
	}
	
	private static float[] parseFloats(int needed, String... strings)
	{
		float[] floats = new float[needed];
		for(int i = 0; i < Math.min(needed, strings.length); ++i)
			try { floats[i] = Float.parseFloat(strings[i]); } catch(Throwable err) {}
		return floats;
	}
	
	private static int[] parseInts(int needed, String... strings)
	{
		int[] ints = new int[needed];
		for(int i = 0; i < Math.min(needed, strings.length); ++i)
			try { ints[i] = Integer.parseInt(strings[i]); } catch(Throwable err) {}
		return ints;
	}
}