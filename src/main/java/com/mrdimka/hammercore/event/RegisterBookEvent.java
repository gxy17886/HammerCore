package com.mrdimka.hammercore.event;

import net.minecraftforge.fml.common.eventhandler.Event;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.bookAPI.Book;

public class RegisterBookEvent extends Event
{
	public void registerBook(Book book)
	{
		HammerCore.bookProxy.registerBookInstance(book);
	}
	
	public boolean isBookRegistered(String id)
	{
		return HammerCore.bookProxy.getBookInstanceById(id) != null;
	}
}