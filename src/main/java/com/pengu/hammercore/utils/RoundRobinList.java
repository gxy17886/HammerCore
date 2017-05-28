package com.pengu.hammercore.utils;

import java.util.ArrayList;

public class RoundRobinList<E> extends ArrayList<E>
{
	private int lastIndex;
	
	public E next()
	{
		E e = get(lastIndex);
		skip(1);
		return e;
	}
	
	public void skip(int steps)
	{
		lastIndex = (lastIndex + steps) % size();
	}
}