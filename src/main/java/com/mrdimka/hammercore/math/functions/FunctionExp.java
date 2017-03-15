package com.mrdimka.hammercore.math.functions;

public final class FunctionExp extends ExpressionFunction
{
	public static final FunctionExp inst = new FunctionExp();
	
	private FunctionExp()
	{
		super("exp");
	}
	
	@Override
	public double apply(double x)
	{
		return Math.exp(x);
	}
}