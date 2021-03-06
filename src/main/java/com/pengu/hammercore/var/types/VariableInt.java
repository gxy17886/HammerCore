package com.pengu.hammercore.var.types;

import java.util.Objects;

import net.minecraft.nbt.NBTTagCompound;

import com.pengu.hammercore.var.IVariable;

public class VariableInt implements IVariable<Integer>
{
	final String id;
	Integer var, prevVar;
	
	public VariableInt(String id)
	{
		this.id = id;
	}
	
	@Override
	public Integer get()
	{
		return var;
	}
	
	@Override
	public void set(Integer t)
	{
		prevVar = var;
		var = t;
	}
	
	public void setInt(int i)
	{
		set(i);
	}
	
	public int getInt()
	{
		return get() != null ? get() : 0;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("Var", getInt());
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		setInt(nbt.getInteger("Var"));
	}
	
	@Override
	public boolean hasChanged()
	{
		return !Objects.equals(var, prevVar);
	}
	
	@Override
	public void setNotChanged()
	{
		prevVar = var;
	}
	
	@Override
	public String getId()
	{
		return id;
	}
	
}