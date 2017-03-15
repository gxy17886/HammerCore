package com.mrdimka.hammercore.math.functions;

import java.security.SecureRandom;

public final class FunctionRand extends ExpressionFunction
{
	//Create unrepeatable random. Cannot be the same.
	private static final SecureRandom rand = new SecureRandom(((System.currentTimeMillis() + System.nanoTime()) + "").getBytes());
	
	public static final FunctionRand inst = new FunctionRand();
	
	private FunctionRand()
	{
		super("rand");
	}
	
	@Override
	public double apply(double x)
	{
		return (((double) rand.nextInt(Integer.MAX_VALUE)) / ((double) Integer.MAX_VALUE)) * x;
	}
}