package com.mrdimka.hammercore.math.functions;

public final class FunctionAcos extends ExpressionFunction
{
	public static final FunctionAcos inst = new FunctionAcos();
	
	private FunctionAcos()
	{
		super("acos");
	}
	
	@Override
	public double apply(double x)
	{
		return Math.acos(x);
	}
}