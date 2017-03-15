package com.mrdimka.hammercore.math.functions;

public final class FunctionTanh extends ExpressionFunction
{
	public static final FunctionTanh inst = new FunctionTanh();
	
	private FunctionTanh()
	{
		super("tanh");
	}
	
	@Override
	public double apply(double x)
	{
		return Math.tanh(x);
	}
}