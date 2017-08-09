package com.mrdimka.hammercore.client.model.file;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ModelPart implements Serializable
{
	public String name;
	public float textureWidth, textureHeight, rotationPointX, rotationPointY, rotationPointZ, rotateAngleX, rotateAngleY, rotateAngleZ, offsetX, offsetY, offsetZ;
	public int textureOffsetX, textureOffsetY;
	public boolean mirror;
	public Set<ModelPart> childs;
	public Set<ModelCube> boxes;
	
	public void addBox(float x1, float y1, float z1, int x2, int y2, int z2)
	{
		if(boxes == null)
			boxes = new HashSet<>();
		ModelCube cube = new ModelCube();
		cube.posX1 = x1;
		cube.posY1 = y1;
		cube.posZ1 = z1;
		cube.posX2 = x2;
		cube.posY2 = y2;
		cube.posZ2 = z2;
		boxes.add(cube);
	}
	
	public void setRotationPoint(float x, float y, float z)
	{
		rotationPointX = x;
		rotationPointY = y;
		rotationPointZ = z;
	}
}