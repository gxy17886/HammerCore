package com.mrdimka.hammercore.math.functions;

public final class FunctionLog extends ExpressionFunction
{
	public static final FunctionLog inst = new FunctionLog();
	
	private FunctionLog()
	{
		super("log");
	}
	
	@Override
	public double apply(double x)
	{
		return Math.log(x);
	}
}