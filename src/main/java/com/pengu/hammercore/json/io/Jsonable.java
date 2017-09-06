package com.pengu.hammercore.json.io;

import java.io.NotSerializableException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import com.google.common.base.Throwables;
import com.pengu.hammercore.json.JSONException;
import com.pengu.hammercore.json.JSONObject;
import com.pengu.hammercore.json.JSONTokener;

public interface Jsonable
{
	default String serialize()
	{
		StringBuilder b = new StringBuilder();
		
		b.append("{");
		
		for(Field f : getClass().getDeclaredFields())
		{
			f.setAccessible(true);
			
			if(f.getAnnotation(IgnoreSerialization.class) != null || Modifier.isStatic(f.getModifiers()))
				continue;
			
			String name = f.getName();
			
			SerializedName aname = f.getAnnotation(SerializedName.class);
			if(aname != null)
				name = aname.value();
			
			name = formatInsideString(name);
			
			try
			{
				if(f.getType().isPrimitive() || String.class.isAssignableFrom(f.getType()) || Jsonable.class.isAssignableFrom(f.getType()))
				{
					Object val = f.get(this);
					String $ = formatInsideString(val + "");
					if(val instanceof Jsonable)
						$ = ((Jsonable) val).serialize();
					
					if(val instanceof String)
						$ = "\"" + formatInsideString(val + "") + "\"";
					
					b.append("\"" + name + "\":" + $ + ",");
				} else
					throw new NotSerializableException("Field " + f.getName() + " could not be serialized! Please insert @com.pengu.code.json.serapi.IgnoreSerialization !");
			} catch(Throwable err)
			{
				Throwables.propagate(err);
			}
		}
		
		if(b.charAt(b.length() - 1) == ',')
			b = b.deleteCharAt(b.length() - 1);
		
		return b.append("}").toString();
	}
	
	public static String formatInsideString(String text)
	{
		return text.replace("\\", "\\\\").replace("\"", "\\\"");
	}
	
	public static <T extends Jsonable> T deserialize(String json, Class<T> type) throws JSONException
	{
		return deserialize((JSONObject) new JSONTokener(json).nextValue(), type);
	}
	
	public static <T extends Jsonable> T deserialize(JSONObject js, Class<T> type) throws JSONException
	{
		try
		{
			Constructor<T> constr = type.getConstructor();
			constr.setAccessible(true);
			T i = constr.newInstance();
			
			for(Field f : type.getDeclaredFields())
			{
				f.setAccessible(true);
				
				if(f.getAnnotation(IgnoreSerialization.class) != null || Modifier.isStatic(f.getModifiers()))
					continue;
				
				String name = f.getName();
				
				SerializedName aname = f.getAnnotation(SerializedName.class);
				if(aname != null)
					name = aname.value();
				
				if(!js.has(name))
					throw new IllegalStateException("No key \"" + name + "\" found to be deserialized!");
				
				Object obj = js.get(name);
				
				if(JSONObject.NULL.equals(obj))
					f.set(i, null);
				else if(obj instanceof JSONObject && Jsonable.class.isAssignableFrom(f.getType()))
				{
					Class<? extends Jsonable> test = (Class<? extends Jsonable>) f.getType();
					f.set(i, deserialize((JSONObject) obj, test));
				} else
					f.set(i, obj);
			}
			
			return i;
		} catch(NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			Throwables.propagate(e);
		}
		
		return null;
	}
}