package com.mrdimka.hammercore.math.functions;

import java.math.BigDecimal;

public abstract class ExpressionFunction
{
	public final String functionName;
	
	public ExpressionFunction(String funcName)
	{
		functionName = funcName;
	}
	
	public boolean accepts(String functionName, double x)
	{
		return this.functionName.equalsIgnoreCase(functionName);
	}
	
	public boolean accepts(String functionName, BigDecimal x)
	{
		return this.functionName.equalsIgnoreCase(functionName);
	}
	
	public double apply(String functionName, double x)
	{
		return apply(x);
	}
	
	public BigDecimal apply(String functionName, BigDecimal x)
	{
		return new BigDecimal(apply(x.doubleValue()));
	}
	
	@Deprecated
	public double apply(double x)
	{
		return x;
	}
}