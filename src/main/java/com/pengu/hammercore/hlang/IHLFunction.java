package com.pengu.hammercore.hlang;

public interface IHLFunction
{
	Class[] getArgs();
	void run(Object... args);
}