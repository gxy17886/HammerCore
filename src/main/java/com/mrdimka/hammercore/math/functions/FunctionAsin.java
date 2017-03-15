package com.mrdimka.hammercore.math.functions;

public final class FunctionAsin extends ExpressionFunction
{
	public static final FunctionAsin inst = new FunctionAsin();
	
	private FunctionAsin()
	{
		super("asin");
	}
	
	@Override
	public double apply(double x)
	{
		return Math.asin(x);
	}
}