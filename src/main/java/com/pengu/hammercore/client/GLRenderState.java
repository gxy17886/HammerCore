package com.pengu.hammercore.client;

import org.lwjgl.opengl.GL11;

public class GLRenderState
{
	public static final GLRenderState BLEND = new GLRenderState(GL11.GL_BLEND), NORMALIZE = new GLRenderState(GL11.GL_NORMALIZE);
	
	private final int opcode;
	public boolean isEnabled = false;
	
	public GLRenderState(int opcode)
	{
		this.opcode = opcode;
	}
	
	{
		captureState();
	}
	
	public boolean captureState()
	{
		return isEnabled = GL11.glIsEnabled(opcode);
	}
	
	public void on()
	{
		GL11.glEnable(opcode);
	}
	
	public void off()
	{
		GL11.glEnable(opcode);
	}
	
	public void reset()
	{
		if(isEnabled)
			on();
		else
			off();
	}
}