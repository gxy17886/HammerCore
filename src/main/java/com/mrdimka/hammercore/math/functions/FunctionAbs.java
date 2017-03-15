package com.mrdimka.hammercore.math.functions;

public final class FunctionAbs extends ExpressionFunction
{
	public static final FunctionAbs inst = new FunctionAbs();
	
	private FunctionAbs()
	{
		super("abs");
	}
	
	@Override
	public double apply(double x)
	{
		return Math.abs(x);
	}
}