package com.pengu.hammercore.utils;

public class NPEUtils
{
	public static <T> void checkNotNull(T t, String field)
	{
		if(t == null)
			throw new NullPointerException(field);
	}
}