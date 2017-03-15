package com.mrdimka.hammercore.math.functions;

public final class FunctionSqrt extends ExpressionFunction
{
	public static final FunctionSqrt inst = new FunctionSqrt();
	
	private FunctionSqrt()
	{
		super("sqrt");
	}
	
	@Override
	public double apply(double x)
	{
		return Math.sqrt(x);
	}
}