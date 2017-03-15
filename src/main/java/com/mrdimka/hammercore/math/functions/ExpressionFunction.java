package com.mrdimka.hammercore.math.functions;

public abstract class ExpressionFunction
{
	public final String functionName;
	
	public ExpressionFunction(String funcName)
	{
		functionName = funcName;
	}
	
	public abstract double apply(double x);
}