package com.pengu.hammercore.hlang;

import java.util.Map;

public interface IHLScript
{
	Map<String, Class> getVariables();
	Map<String, Object> getVariableValues();
	IHLFunction getFunction();
}