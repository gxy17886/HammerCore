package com.mrdimka.hammercore.math.functions;

public final class FunctionAtan extends ExpressionFunction
{
	public static final FunctionAtan inst = new FunctionAtan();
	
	private FunctionAtan()
	{
		super("atan");
	}
	
	@Override
	public double apply(double x)
	{
		return Math.atan(x);
	}
}