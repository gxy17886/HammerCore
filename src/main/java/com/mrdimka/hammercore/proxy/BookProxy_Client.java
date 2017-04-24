package com.mrdimka.hammercore.proxy;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.bookAPI.Book;
import com.mrdimka.hammercore.bookAPI.BookCategory;
import com.mrdimka.hammercore.bookAPI.BookEntry;
import com.mrdimka.hammercore.bookAPI.pages.BookPageTextPlain;
import com.mrdimka.hammercore.event.RegisterBookEvent;
import com.mrdimka.hammercore.gui.book.GuiBook;
import com.mrdimka.hammercore.init.ModItems;

public class BookProxy_Client extends BookProxy_Common
{
	private final Map<String, Book> books = new HashMap<>();
	
	@Override
	public Object getBookInstanceById(String id)
	{
		return books.get(id);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBookInstance(Book book)
	{
		String id = book.bookId;
		if(books.putIfAbsent(id, book) != null)
			HammerCore.LOG.bigWarn("A mod tried to register book with already used id: " + id + ", registered: " + books.get(id) + ", failed to register: " + book);
	}
	
	@Override
	public void openBookGui(String bookId)
	{
		if(getBookInstanceById(bookId) != null)
			Minecraft.getMinecraft().displayGuiScreen(new GuiBook((Book) getBookInstanceById(bookId)));
	}
	
	@Override
	public void init()
	{
		registerManual();
		
		MinecraftForge.EVENT_BUS.post(new RegisterBookEvent());
	}
	
	private void registerManual()
	{
		Book hammerCoreManual = new Book("hammercore:manual");
		
		BookCategory items = new BookCategory(hammerCoreManual, "items");
		items.setIcon(new ItemStack(ModItems.manual));
		
		BookCategory blocks = new BookCategory(hammerCoreManual, "blocks");
		
		{
			BookEntry entry = new BookEntry(items, "calculatron", "gui.hammercore:manual/items/calculatron.title");
			entry.setIcon(new ItemStack(ModItems.calculatron));
			
			new BookPageTextPlain(entry, "gui.hammercore:manual/items/calculatron.desc");
		}
		
		registerBookInstance(hammerCoreManual);
	}
}