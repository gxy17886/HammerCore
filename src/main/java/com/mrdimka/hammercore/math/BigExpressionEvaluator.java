package com.mrdimka.hammercore.math;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mrdimka.hammercore.common.utils.Chars;
import com.mrdimka.hammercore.math.functions.ExpressionFunction;
import com.mrdimka.hammercore.math.functions.FunctionMath;

public class BigExpressionEvaluator
{
	private final String str;
	private int pos = -1, ch;
	
	private final List<ExpressionFunction> functions = new ArrayList<>();
	
	{
		// ADD FUNCTIONS. rand(num) is very good, if you try :P
		addFunction(FunctionMath.inst);
	}
	
	public BigExpressionEvaluator(String str)
	{
		str = str.replaceAll(Chars.PI + "", "PI");
		str = str.replaceAll("PI", Math.PI + ""); // Include Math.PI into this
		                                          // expression
		str = str.replaceAll("E", Math.E + ""); // Include Math.E (Euler's
		                                        // number) into this expression
		this.str = str;
	}
	
	private void nextChar()
	{
		ch = (++pos < str.length()) ? str.charAt(pos) : -1;
	}
	
	private boolean eat(int charToEat)
	{
		while(ch == ' ')
			nextChar();
		if(ch == charToEat)
		{
			nextChar();
			return true;
		}
		return false;
	}
	
	/**
	 * Parses expression. Uses + - * / ^ % and all functions defined by
	 * {@link #addFunction(ExpressionFunction)}
	 */
	public final BigDecimal parse()
	{
		pos = -1;
		
		nextChar();
		BigDecimal x = parseExpression();
		if(pos < str.length())
			throw new RuntimeException("Unexpected: " + (char) ch);
		return x;
	}
	
	// Grammar:
	// expression = term | expression `+` term | expression `-` term
	// term = factor | term `*` factor | term `/` factor
	// factor = `+` factor | `-` factor | `(` expression `)`
	// | number | functionName factor | factor `^` factor
	private BigDecimal parseExpression()
	{
		BigDecimal x = parseTerm();
		for(;;)
		{
			if(eat('+'))
				x = x.add(parseTerm()); // addition
			else if(eat('-'))
				x = x.subtract(parseTerm()); // subtraction
			else
				return x;
		}
	}
	
	private BigDecimal parseTerm()
	{
		BigDecimal x = parseFactor();
		for(;;)
		{
			if(eat('*'))
				x = x.multiply(parseFactor()); // multiplication
			else if(eat('/') || eat(':'))
				x = x.divide(parseFactor()); // division
			else if(eat('%'))
				x = x.remainder(parseFactor()); // mod
			else if(eat('^'))
				x = x.pow(parseFactor().intValue()); // exponentiation
			else
				return x;
		}
	}
	
	private BigDecimal parseFactor()
	{
		if(eat('+'))
			return parseFactor(); // unary plus
		if(eat('-'))
			return parseFactor().negate(); // unary minus
			
		BigDecimal x;
		int startPos = this.pos;
		if(eat('(')) // parentheses
		{
			x = parseExpression();
			eat(')');
		} else if((ch >= '0' && ch <= '9') || ch == '.') // numbers
		{
			while((ch >= '0' && ch <= '9') || ch == '.')
				nextChar();
			x = new BigDecimal(str.substring(startPos, this.pos));
		} else if(ch >= 'a' && ch <= 'z') // functions
		{
			while(ch >= 'a' && ch <= 'z')
				nextChar();
			String func = str.substring(startPos, this.pos).toLowerCase();
			x = parseFactor();
			
			boolean funcFound = false;
			for(ExpressionFunction f : functions)
				if(f.accepts(func, x))
				{
					x = f.apply(func, x);
					funcFound = true;
					break;
				}
			
			if(!funcFound)
				throw new RuntimeException("Unknown function: " + func);
		} else
			throw new RuntimeException("Unexpected: " + (char) ch);
		return x;
	}
	
	/**
	 * Adds function to evaluation process.
	 */
	public void addFunction(ExpressionFunction func)
	{
		if(functions.contains(func))
			return; // Don't add duplicates - Bad!
		functions.add(func);
	}
	
	/**
	 * Evaluates expression with optional functions passed. If result is equal
	 * to floor() function, an int will be returned
	 */
	public static String evaluate(String expression, ExpressionFunction... functions)
	{
		BigDecimal result = evaluateDouble(expression, functions);
		if(result.toString().equals(result.toBigInteger().toString()))
			return result.toBigInteger() + "";
		return result + "";
	}
	
	/**
	 * Evaluates expression with optional functions passed. Returns exact
	 * number, in double
	 */
	public static BigDecimal evaluateDouble(String expression, ExpressionFunction... functions)
	{
		try
		{
			return new BigDecimal(expression);
		} catch(Throwable err)
		{
		}
		BigExpressionEvaluator eval = new BigExpressionEvaluator(expression);
		for(ExpressionFunction func : functions)
			eval.addFunction(func);
		return eval.parse();
	}
}