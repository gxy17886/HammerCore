package com.pengu.hammercore.common.items;

import com.pengu.hammercore.bookAPI.ItemBook;

public class ItemHammerCoreManual extends ItemBook
{
	public ItemHammerCoreManual()
	{
		setUnlocalizedName("manual");
		setMaxStackSize(1);
	}
	
	@Override
	public String getBookId()
	{
		return "hammercore:manual";
	}
}