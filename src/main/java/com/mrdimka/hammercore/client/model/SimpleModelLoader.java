package com.mrdimka.hammercore.client.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.client.model.file.ModelCube;
import com.mrdimka.hammercore.client.model.file.ModelFile;
import com.mrdimka.hammercore.client.model.file.ModelPart;

@SideOnly(Side.CLIENT)
public class SimpleModelLoader
{
	/**
	 * Converts string with model data to a model
	 **/
	public static ModelBase convert(String raw, boolean ignoreIssues) throws ModelLoadingException
	{
		if(!raw.contains("\n")) raw = raw.replaceAll(";", "\n");
		String lines[] = raw.split("\n");
		SimpleModel model = new SimpleModel();
		List<ModelRenderer> ended = new ArrayList<ModelRenderer>();
		Map<String, ModelRenderer> renderers = new HashMap<String, ModelRenderer>();
		int ln = 0;
		for(String line : lines)
		{
			ln++;
			try
			{
				if(line.isEmpty() || line.startsWith("//")) continue;
				if(line.startsWith("textureWidth ")) model.textureWidth = Integer.parseInt(line.substring("textureWidth ".length()));
				if(line.startsWith("textureHeight ")) model.textureHeight = Integer.parseInt(line.substring("textureHeight ".length()));
				
				if(line.toLowerCase().startsWith("start "))
				{
					String l = line.substring("start ".length());
					String name = l.substring(0, l.indexOf("("));
					String[] args = l.substring(l.indexOf("(") + 1, l.lastIndexOf(")")).replaceAll(" ", "").split(",");
					int[] data = parseInts(2, args);
					ModelRenderer r = new ModelRenderer(model, name);
					r.setTextureSize(data[0], data[1]);
					renderers.put(name, r);
				}
				
				if(line.toLowerCase().startsWith("addBox "))
				{
					String l = line.substring("addBox ".length());
					String name = l.substring(0, l.indexOf("("));
					String[] args = l.substring(l.indexOf("(") + 1, l.lastIndexOf(")")).replaceAll(" ", "").split(",");
					ModelRenderer renderer = renderers.get(name);
					if(renderer == null) throw new ModelLoadingExceptionMessage("There is no such model renderer named \"" + name + "\"!");
					float[] xyz = parseFloats(3, args);
					int[] size = parseInts(4, Arrays.copyOfRange(args, 3, args.length));
					renderer.addBox(xyz[0], xyz[1], xyz[2], size[0], size[1], size[2]);
				}
				
				if(line.toLowerCase().startsWith("rotationPoint "))
				{
					String l = line.substring("rotationPoint ".length());
					String name = l.substring(0, l.indexOf("("));
					String[] args = l.substring(l.indexOf("(") + 1, l.lastIndexOf(")")).replaceAll(" ", "").split(",");
					ModelRenderer renderer = renderers.get(name);
					if(renderer == null) throw new ModelLoadingExceptionMessage("There is no such model renderer named \"" + name + "\"!");
					float[] xyz = parseFloats(3, args);
					renderer.setRotationPoint(xyz[0], xyz[1], xyz[2]);
				}
				
				if(line.toLowerCase().startsWith("textureSize "))
				{
					String l = line.substring("textureSize ".length());
					String name = l.substring(0, l.indexOf("("));
					String[] args = l.substring(l.indexOf("(") + 1, l.lastIndexOf(")")).replaceAll(" ", "").split(",");
					ModelRenderer renderer = renderers.get(name);
					if(renderer == null) throw new ModelLoadingExceptionMessage("There is no such model renderer named \"" + name + "\"!");
					float[] xy = parseFloats(2, args);
					renderer.textureWidth = xy[0];
					renderer.textureHeight = xy[1];
				}
				
				if(line.toLowerCase().startsWith("mirror "))
				{
					String l = line.substring("mirror ".length());
					String name = l.substring(0, l.indexOf("("));
					String[] args = l.substring(l.indexOf("(") + 1, l.lastIndexOf(")")).replaceAll(" ", "").split(",");
					ModelRenderer renderer = renderers.get(name);
					if(renderer == null) throw new ModelLoadingExceptionMessage("There is no such model renderer named \"" + name + "\"!");
					boolean[] mirror = parseBooleans(1, args);
					renderer.mirror = mirror[0];
				}
				
				if(line.toLowerCase().startsWith("end "))
				{
					String shape = line.substring("end ".length());
					ModelRenderer renderer = renderers.remove(shape);
					if(renderer == null) throw new ModelLoadingExceptionMessage("There is no such model renderer named \"" + shape + "\"!");
					ended.add(renderer);
				}
			}
			catch(ModelLoadingExceptionMessage err)
			{
				String msg = "Can't parse model line #" + ln + ": " + err.getMessage();
				
				if(ignoreIssues) System.err.println(msg);
				else throw new ModelLoadingException(msg);
			}
			catch(Throwable err)
			{
				String msg = "Can't parse model line #" + ln + ": " + err.getClass().getName() + " > " + err.getMessage();
				
				if(ignoreIssues) System.err.println(msg);
				else throw new ModelLoadingException(msg);
			}
		}
		
		for(int i = 0; i < ended.size(); ++i)
		{
			ModelRenderer e = ended.get(i);
			if(e.textureWidth == 0F || e.textureHeight == 0F)
			{
				e.textureWidth = model.textureWidth;
				e.textureHeight = model.textureHeight;
			}
			model.boxList.add(e);
		}
		
		return model;
	}
	
	public static SimpleModel convert(ModelFile file)
	{
		SimpleModel model = new SimpleModel();
		model.textureWidth = file.textureWidth;
		model.textureHeight = file.textureHeight;
		for(ModelPart part : file.parts) model.boxList.add(toRenderer(model, part));
		return model;
	}
	
	public static ModelRenderer toRenderer(ModelBase parent, ModelPart part)
	{
		ModelRenderer r = new ModelRenderer(parent, part.name).setTextureOffset(part.textureOffsetX, part.textureOffsetY);
		r.offsetX = part.offsetX;
		r.offsetY = part.offsetY;
		r.offsetZ = part.offsetZ;
		r.rotateAngleX = part.rotateAngleX;
		r.rotateAngleY = part.rotateAngleY;
		r.rotateAngleZ = part.rotateAngleZ;
		r.mirror = part.mirror;
		for(ModelCube cube : part.boxes) r.addBox(cube.boxName, cube.posX1, cube.posY1, cube.posZ1, (int) (cube.posX2 - cube.posX1), (int) (cube.posY2 - cube.posY1), (int) (cube.posZ2 - cube.posZ1));
		for(ModelPart child : part.childs) r.childModels.add(toRenderer(parent, child));
		return r;
	}
	
	public static ModelPart toPart(ModelRenderer part)
	{
		ModelPart r = new ModelPart();
		r.boxes = new HashSet<ModelCube>();
		r.childs = new HashSet<ModelPart>();
		r.name = part.boxName;
		r.offsetX = part.offsetX;
		r.offsetY = part.offsetY;
		r.offsetZ = part.offsetZ;
		r.rotateAngleX = part.rotateAngleX;
		r.rotateAngleY = part.rotateAngleY;
		r.rotateAngleZ = part.rotateAngleZ;
		r.mirror = part.mirror;
		
		r.rotationPointX = part.rotationPointX;
		r.rotationPointY = part.rotationPointY;
		r.rotationPointZ = part.rotationPointZ;
		
		try
		{
			Field f = ModelRenderer.class.getDeclaredFields()[2];
			f.setAccessible(true);
			r.textureOffsetX = f.getInt(part);
			
			f = ModelRenderer.class.getDeclaredFields()[3];
			f.setAccessible(true);
			r.textureOffsetY = f.getInt(part);
		}
		catch(Throwable err) {}
		
		for(ModelBox box : part.cubeList)
		{
			ModelCube cube = new ModelCube();
			cube.boxName = box.boxName;
			cube.posX1 = box.posX1;
			cube.posY1 = box.posY1;
			cube.posZ1 = box.posZ1;
			cube.posX2 = box.posX2;
			cube.posY2 = box.posY2;
			cube.posZ2 = box.posZ2;
			r.boxes.add(cube);
		}
		
		if(part.childModels != null && !part.childModels.isEmpty()) for(ModelRenderer child : part.childModels) r.childs.add(toPart(child));
		return r;
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
	
	/**
	 * Converts model into string, that can be stored in a file.
	 **/
	public static String convert(ModelBase model)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("# Model converted from " + model.getClass() + " using native Hammer Core");
		sb.append('\n');
		sb.append('\n');
		sb.append("# Defining texture dimensions");
		sb.append('\n');
		sb.append("textureWidth " + model.textureWidth + ";");
		sb.append('\n');
		sb.append("textureHeight " + model.textureHeight + ";");
		sb.append('\n');
		sb.append('\n');
		
		for(int i = 0; i < model.boxList.size(); ++i)
		{
			ModelRenderer r = (ModelRenderer) model.boxList.get(i);
			sb.append("# Model part \"" + r.boxName + "\"");
			sb.append("start " + r.boxName + "(" + ((int) r.textureWidth) + ", " + ((int) r.textureHeight) + ");");
			sb.append('\n');
			
			for(int bi = 0; bi < r.cubeList.size(); ++bi)
			{
				ModelBox box = (ModelBox) r.cubeList.get(bi);
				sb.append("addBox " + r.boxName + "(" + box.posX1 + ", " + box.posY1 + ", " + box.posZ1 + ", " + ((int) (box.posX2 - box.posX1)) + ", " + ((int) (box.posY2 - box.posY1)) + ", " + ((int) (box.posZ2 - box.posZ1)) + ");");
				sb.append('\n');
			}
			
			sb.append("rotationPoint " + r.boxName + "(" + r.rotationPointX + ", " + r.rotationPointY + ", " + r.rotationPointZ + ");");
			sb.append('\n');
			sb.append("textureSize " + r.boxName + "(" + r.textureWidth + ", " + r.textureHeight + ");");
			sb.append('\n');
			sb.append("mirror " + r.boxName + "(" + r.mirror + ");");
			sb.append('\n');
			sb.append("end " + r.boxName + ";");
			
			if(i < model.boxList.size() - 1)
			{
				sb.append('\n');
				sb.append('\n');
			}
		}
		
		return sb.toString();
	}
	
	public static ModelFile convertToFile(ModelBase model)
	{
		ModelFile file = new ModelFile();
		file.parts = new HashSet<ModelPart>();
		file.textureWidth = model.textureWidth;
		file.textureHeight = model.textureHeight;
		for(int i = 0; i < model.boxList.size(); ++i) file.parts.add(toPart(model.boxList.get(i)));
		return file;
	}
	
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
	
	public static class ModelLoadingException extends Exception
	{
		public ModelLoadingException(String reason)
		{
			super(reason);
		}
	}
	
	public static class ModelLoadingExceptionMessage extends Exception
	{
		public ModelLoadingExceptionMessage(String reason)
		{
			super(reason);
		}
	}
}