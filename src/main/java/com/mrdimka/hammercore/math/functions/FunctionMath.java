package com.mrdimka.hammercore.math.functions;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

import scala.math.BigInt;

/**
 * All functions of the past are contained in this small class
 */
public class FunctionMath extends ExpressionFunction
{
	// Create unrepeatable random. Cannot be the same.
	private static final SecureRandom rand = new SecureRandom(((System.currentTimeMillis() + System.nanoTime()) + "").getBytes());
	
	public static final FunctionMath inst = new FunctionMath();
	
	public FunctionMath()
	{
		super("Math");
	}
	
	private final Set<String> allowedFuncs = new HashSet<>();
	
	{
		for(Method m : Math.class.getMethods())
			if(Modifier.isStatic(m.getModifiers()) && Modifier.isPublic(m.getModifiers()) && m.getParameterTypes().length == 1 && (m.getParameterTypes()[0] == double.class || m.getParameterTypes()[0] == Double.class))
				allowedFuncs.add(m.getName());
	}
	
	@Override
	public boolean accepts(String functionName, double x)
	{
		functionName = functionName.toLowerCase();
		return allowedFuncs.contains(functionName) || functionName.equals("rand");
	}
	
	@Override
	public boolean accepts(String functionName, BigDecimal x)
	{
		functionName = functionName.toLowerCase();
		return allowedFuncs.contains(functionName) || functionName.equals("rand");
	}
	
	@Override
	public double apply(String functionName, double x)
	{
		functionName = functionName.toLowerCase();
		
		if(functionName.equals("rand"))
			return (((double) rand.nextInt(Integer.MAX_VALUE)) / ((double) Integer.MAX_VALUE)) * x;
		
		try
		{
			return (Double) Math.class.getMethod(functionName, double.class).invoke(null, x);
		} catch(Throwable er)
		{
		}
		
		return x;
	}
	
	private static final double LOG10 = Math.log(10.0);
	private static final double LOG2 = Math.log(2.0);
	public static MathContext context = new MathContext(100);
	
	@Override
	public BigDecimal apply(String functionName, BigDecimal x)
	{
		functionName = functionName.toLowerCase();
		
		if(functionName.equals("rand"))
			return x.multiply(new BigDecimal(((double) rand.nextInt(Integer.MAX_VALUE)) / ((double) Integer.MAX_VALUE)));
		
		if(functionName.equals("log"))
		{
			BigInteger val = x.unscaledValue();
			int blex = val.bitLength() - 1022;
			if(blex > 0)
				val = val.shiftRight(blex);
			double res = Math.log(val.doubleValue());
			return new BigDecimal((blex > 0 ? res + blex * LOG2 : res) + x.scale() * LOG10);
		}
		
		return x;
	}
	
	{
		for(String s : allowedFuncs)
			System.out.println(s);
	}
}