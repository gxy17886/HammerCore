package com.mrdimka.hammercore.math.functions;

public final class FunctionSinh extends ExpressionFunction
{
	public static final FunctionSinh inst = new FunctionSinh();
	
	private FunctionSinh()
	{
		super("sinh");
	}
	
	@Override
	public double apply(double x)
	{
		return Math.sinh(x);
	}
}