package com.mrdimka.hammercore.api.handlers;

import com.mrdimka.hammercore.common.utils.WorldUtil;

/**
 * Abstract handler that can contain any method you need.
 */
public interface ITileHandler
{
	default <T extends ITileHandler> T get(Class<T> handler)
	{
		return WorldUtil.cast(this, handler);
	}
}