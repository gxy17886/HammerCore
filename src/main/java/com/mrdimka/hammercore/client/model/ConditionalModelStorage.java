package com.mrdimka.hammercore.client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.model.ModelBase;

import com.mrdimka.hammercore.client.model.CasedModel;

public class ConditionalModelStorage
{
	public final CasedModel source;
	public final List<Object> conditions = new ArrayList<Object>();
	private final List<Object> lastConditions = new ArrayList<Object>();
	public ModelBase baked;
	
	public ConditionalModelStorage(CasedModel src)
	{
		source = src;
	}
	
	public void setConditions(Collection conditions)
	{
		this.conditions.clear();
		this.conditions.addAll(conditions);
		update();
	}
	
	public void update()
	{
		if(!conditions.equals(lastConditions) && source != null)
		{
			baked = source.produce(conditions);
			lastConditions.clear();
			lastConditions.addAll(conditions);
		}
	}
	
	public ModelBase getBaked()
	{
		return baked;
	}
}