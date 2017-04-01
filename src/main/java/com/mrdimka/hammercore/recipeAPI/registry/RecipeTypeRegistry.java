package com.mrdimka.hammercore.recipeAPI.registry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.common.utils.JSONObjectToNBT;
import com.mrdimka.hammercore.json.JSONArray;
import com.mrdimka.hammercore.json.JSONException;
import com.mrdimka.hammercore.json.JSONTokener;
import com.mrdimka.hammercore.recipeAPI.types.IRecipeType;

public class RecipeTypeRegistry implements IRecipeTypeRegistry
{
	private final Set<IRecipeType> types = new HashSet<>();
	
	@Override
	public void register(IRecipeType type)
	{
		types.add(type);
	}
	
	public void forEach(Consumer<IRecipeType> cycle)
	{
		types.stream().forEach(cycle);
	}
	
	public IRecipeScript parseAll(String[] jsons) throws JSONException
	{
		List<SimpleRecipeScript> scripts = new ArrayList<>();
		for(String json : jsons) scripts.add(parse(json));
		return new GlobalRecipeScript(scripts.toArray(new SimpleRecipeScript[scripts.size()]));
	}
	
	public SimpleRecipeScript parse(String json) throws JSONException
	{
		return parse(JSONObjectToNBT.convert((JSONArray) new JSONTokener(json).nextValue()));
	}
	
	public SimpleRecipeScript parse(NBTTagList list)
	{
		final SimpleRecipeScript script = new SimpleRecipeScript();
		script.makeTag = list.copy();
		
		for(int i = 0; i < list.tagCount(); ++i)
		{
			NBTTagCompound nbt = list.getCompoundTagAt(i);
			
			String id = nbt.getString("id");
			NBTTagCompound r = nbt.getCompoundTag("recipe");
			
			boolean[] parsed = new boolean[1];
			
			forEach(t ->
			{
				if(t.getTypeId().equals(id))
				{
					parsed[0] = true;
					Object o = t.createRecipe(r);
					script.types.put(o, t);
					if(nbt.getBoolean("remove")) script.swaps.add(o);
				}
			});
			
			if(!parsed[0]) HammerCore.LOG.warn("Warning: Found non-existing/missing recipe type: " + id + "! This will get ignored.");
		}
		
		return script;
	}
}