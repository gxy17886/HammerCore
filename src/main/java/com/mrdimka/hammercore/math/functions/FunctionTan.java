package com.mrdimka.hammercore.math.functions;

public final class FunctionTan extends ExpressionFunction
{
	public static final FunctionTan inst = new FunctionTan();
	
	private FunctionTan()
	{
		super("tan");
	}
	
	@Override
	public double apply(double x)
	{
		return Math.tan(Math.toRadians(x));
	}
}