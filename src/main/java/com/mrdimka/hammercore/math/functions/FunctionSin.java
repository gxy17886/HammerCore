package com.mrdimka.hammercore.math.functions;

public final class FunctionSin extends ExpressionFunction
{
	public static final FunctionSin inst = new FunctionSin();
	
	private FunctionSin()
	{
		super("sin");
	}
	
	@Override
	public double apply(double x)
	{
		return Math.sin(Math.toRadians(x));
	}
}