package com.mrdimka.hammercore.math.functions;

public final class FunctionCosh extends ExpressionFunction
{
	public static final FunctionCosh inst = new FunctionCosh();
	
	private FunctionCosh()
	{
		super("cosh");
	}
	
	@Override
	public double apply(double x)
	{
		return Math.cosh(x);
	}
}