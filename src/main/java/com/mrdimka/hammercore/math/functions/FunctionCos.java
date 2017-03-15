package com.mrdimka.hammercore.math.functions;

public final class FunctionCos extends ExpressionFunction
{
	public static final FunctionCos inst = new FunctionCos();
	
	private FunctionCos()
	{
		super("cos");
	}
	
	@Override
	public double apply(double x)
	{
		return Math.cos(Math.toRadians(x));
	}
}