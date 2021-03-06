package com.pengu.hammercore.common.utils;

public class DynamicObject<OBJ>
{
	private OBJ obj;
	
	public DynamicObject(Class<OBJ> type)
	{
		reset(type);
	}
	
	public DynamicObject(OBJ obj)
	{
		if(obj != null)
			reset(obj.getClass());
		this.obj = obj;
	}
	
	private void reset(Class type)
	{
		this.obj = null;
		if(String.class.isAssignableFrom(type))
			this.obj = (OBJ) "";
		if(Byte.class.isAssignableFrom(type))
			this.obj = (OBJ) Byte.valueOf((byte) 0);
		if(Short.class.isAssignableFrom(type))
			this.obj = (OBJ) Short.valueOf((short) 0);
		if(Integer.class.isAssignableFrom(type))
			this.obj = (OBJ) Integer.valueOf((int) 0);
		if(Long.class.isAssignableFrom(type))
			this.obj = (OBJ) Long.valueOf((long) 0);
		if(Float.class.isAssignableFrom(type))
			this.obj = (OBJ) Float.valueOf((float) 0);
		if(Double.class.isAssignableFrom(type))
			this.obj = (OBJ) Double.valueOf((double) 0);
		if(Boolean.class.isAssignableFrom(type))
			this.obj = (OBJ) Boolean.valueOf(false);
	}
	
	public OBJ get()
	{
		return obj;
	}
	
	public void set(OBJ obj)
	{
		if(obj != null)
			reset(obj.getClass());
		this.obj = obj;
	}
}