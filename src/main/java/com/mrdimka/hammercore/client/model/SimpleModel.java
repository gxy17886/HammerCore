package com.mrdimka.hammercore.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class SimpleModel extends ModelBase
{
	public void addBox(ModelRenderer box)
	{
		boxList.add(box);
	}
	
	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		for(int i = 0; i < boxList.size(); ++i)
		{
			ModelRenderer mr = (ModelRenderer) boxList.get(i);
			mr.render(f5);
		}
	}
}