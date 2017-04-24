package com.mrdimka.hammercore.client.renderer.shader;

public interface IShaderOperation
{
	boolean load(ShaderProgram program);
	void operate(ShaderProgram program);
	int operationID();
}