package com.mrdimka.hammercore.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.bookAPI.Book;

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