package com.mrdimka.hammercore.gui.book;

import java.io.IOException;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.SoundEvents;

import org.lwjgl.opengl.GL11;

import com.mrdimka.hammercore.bookAPI.Book;
import com.mrdimka.hammercore.bookAPI.BookCategory;
import com.mrdimka.hammercore.client.GLRenderState;
import com.mrdimka.hammercore.client.utils.RenderUtil;
import com.mrdimka.hammercore.gui.GuiCentered;

public class GuiBook extends GuiCentered
{
	public final Book book;
	
	public GuiBook(Book book)
	{
		this.book = book;
		xSize = book.width;
		ySize = book.height;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GLRenderState.BLEND.on();
		mc.getTextureManager().bindTexture(book.customBackground);
		RenderUtil.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		int FONT_HEIGHT = fontRenderer.FONT_HEIGHT;
		
		int y = 0;
		for(BookCategory cat : book.categories)
		{
			fontRenderer.drawString(cat.getTitle(), (int) guiLeft + 12 + (!cat.getIcon().isEmpty() ? FONT_HEIGHT : 0), (int) guiTop + 14 + y, 0, false);
			if(!cat.getIcon().isEmpty())
			{
				GL11.glPushMatrix();
				GlStateManager.disableLighting();
				RenderHelper.enableGUIStandardItemLighting();
				GL11.glTranslated(guiLeft + 11, guiTop + 13 + y, 0);
				GL11.glScaled(1 / 16D * FONT_HEIGHT, 1 / 16D * FONT_HEIGHT, 1);
				itemRender.renderItemAndEffectIntoGUI(cat.getIcon(), 0, 0);
				GlStateManager.disableLighting();
				GL11.glPopMatrix();
			}
			y += fontRenderer.FONT_HEIGHT + 4;
		}
		
		y = 0;
		for(BookCategory cat : book.categories)
		{
			if(mouseX >= guiLeft + 10 && mouseY >= guiTop + 12 + y && mouseX < guiLeft + 124 && mouseY < guiTop + 14 + y + fontRenderer.FONT_HEIGHT)
			{
				GL11.glColor4f(1, 1, 1, 1);
				mc.getTextureManager().bindTexture(book.customBackground);
				RenderUtil.drawTexturedModalRect(guiLeft + 10, guiTop + 12 + y, 146, 0, 110, 11);
			}
			
			y += fontRenderer.FONT_HEIGHT + 4;
		}
		GLRenderState.BLEND.off();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		int y = 0;
		if(mouseButton == 0)
			for(BookCategory cat : book.categories)
			{
				if(mouseX >= guiLeft + 10 && mouseY >= guiTop + 12 + y && mouseX < guiLeft + 124 && mouseY < guiTop + 14 + y + fontRenderer.FONT_HEIGHT)
				{
					mc.displayGuiScreen(new GuiBookCategory(this, cat));
					mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
					break;
				}
				
				y += fontRenderer.FONT_HEIGHT + 4;
			}
	}
}