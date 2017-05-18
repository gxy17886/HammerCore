package com.mrdimka.hammercore.client.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;

import com.mrdimka.hammercore.client.model.file.ModelCube;
import com.mrdimka.hammercore.client.model.file.ModelPart;

public class CasedModel
{
	public Map<ModelPart, String> PREDICATES = new HashMap<>();
	public int textureHeight, textureWidth;
	
	public void addBox(ModelPart box, String condition)
	{
		PREDICATES.put(box, condition);
	}
	
	public SimpleModel produce(String[] arguments)
	{
		return produce(Arrays.asList((Object[]) arguments));
	}
	
	public SimpleModel produce(List arguments)
	{
		SimpleModel model = new SimpleModel();
		model.textureWidth = textureWidth;
		model.textureHeight = textureHeight;
		for(ModelPart mr : PREDICATES.keySet())
			try
			{
				if(conditionsMatch(generateConditions(PREDICATES.get(mr)), arguments))
					model.addBox(SimpleModelLoader.toRenderer(model, mr));
			} catch(Throwable err)
			{
			}
		return model;
	}
	
	public static boolean conditionsMatch(String conditions[], List arguments_)
	{
		List<String> arguments = new ArrayList<>();
		if(conditions == null || conditions.length == 0)
			return true;
		for(Object o : arguments_)
			arguments.add((o + "").toLowerCase());
		
		if(conditions[0] == "o")
		{
			for(int i = 1; i < conditions.length; ++i)
			{
				String c = conditions[i];
				if(c.startsWith("!") && !arguments.contains(c.substring(1)))
					return true;
				else if(arguments.contains(c))
					return true;
			}
		} else if(conditions[0] == "a" || conditions[0] == "s")
		{
			for(int i = 1; i < conditions.length; ++i)
			{
				String c = conditions[i];
				if(c.startsWith("!") && arguments.contains(c.substring(1)))
					return false;
				else if(!arguments.contains(c))
					return false;
			}
			
			return true;
		}
		
		return false;
	}
	
	public static String[] generateConditions(String condition)
	{
		if(condition == null || condition.isEmpty())
			return new String[0];
		condition = condition.trim();
		
		String parsed = null;
		
		if(condition.contains(" or "))
		{
			parsed = "o;";
			String[] conds = condition.split(" or ");
			for(String c : conds)
				parsed += c + ";";
		} else if(condition.contains(" and "))
		{
			parsed = "a;";
			String[] conds = condition.split(" and ");
			for(String c : conds)
				parsed += c + ";";
		} else
		{
			parsed = "s;" + condition;
		}
		
		if(parsed.endsWith(";"))
			parsed = parsed.substring(0, parsed.length() - 1);
		
		return parsed.split(";");
	}
}