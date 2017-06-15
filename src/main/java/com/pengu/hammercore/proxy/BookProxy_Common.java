package com.pengu.hammercore.proxy;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.hammercore.bookAPI.Book;

public class BookProxy_Common
{
	public Object getBookInstanceById(String id)
	{
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBookInstance(Book book)
	{
	}
	
	public void openBookGui(String bookId)
	{
	}
	
	public void init()
	{
	}
}