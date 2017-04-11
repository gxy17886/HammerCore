package com.mrdimka.hammercore.proxy;

import com.mrdimka.hammercore.api.dynlight.DynamicLightGetter;
import com.mrdimka.hammercore.api.dynlight.IDynlightSrc;

public class LightProxy_Client extends LightProxy_Common
{
	@Override
	public void addDynLight(IDynlightSrc src)
	{
		DynamicLightGetter.addLightSource(src);
	}
	
	@Override
	public void removeDynLight(IDynlightSrc src)
	{
		DynamicLightGetter.removeLightSource(src);
	}
}