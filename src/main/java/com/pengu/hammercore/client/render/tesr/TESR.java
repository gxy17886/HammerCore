package com.pengu.hammercore.client.render.tesr;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;

import com.mrdimka.hammercore.client.utils.RenderBlocks;
import com.mrdimka.hammercore.tile.TileSyncable;
import com.pengu.hammercore.client.DestroyStageTexture;
import com.pengu.hammercore.client.render.item.IItemRender;

public abstract class TESR<T extends TileEntity> extends TileEntitySpecialRenderer<T> implements IItemRender
{
	/** This is safe to use while rendering */
	protected float destroyProgress;
	protected Minecraft mc = Minecraft.getMinecraft();
	
	@Override
	public final void renderTileEntityAt(T te, double x, double y, double z, float partialTicks, int destroyStage)
	{
		ResourceLocation destroy = null;
		RayTraceResult over = mc.objectMouseOver;
		destroyProgress = 0;
		if(over != null && over.typeOfHit == Type.BLOCK && over.getBlockPos().equals(te.getPos()))
		{
			float progress = destroyProgress = Minecraft.getMinecraft().playerController.curBlockDamageMP;
			if(progress > 0F)
				destroy = DestroyStageTexture.getByProgress(progress);
		}
		
		renderTileEntityAt(te, x, y, z, partialTicks, destroy);
	}
	
	@Override
	public final void renderTileEntityFast(T te, double x, double y, double z, float partialTicks, int destroyStage, VertexBuffer buffer)
	{
		ResourceLocation destroy = null;
		RayTraceResult over = mc.objectMouseOver;
		if(over != null && over.typeOfHit == Type.BLOCK && over.getBlockPos().equals(te.getPos()))
		{
			float progress = Minecraft.getMinecraft().playerController.curBlockDamageMP;
			if(progress > 0F)
				destroy = DestroyStageTexture.getByProgress(progress);
		}
		renderTileEntityFast(te, x, y, z, partialTicks, destroy, buffer);
	}
	
	/**
	 * Return true for Hammer Core to use NBT rendering instead of TileEntity
	 * rendering.
	 **/
	public boolean canRenderFromNbt()
	{
		return false;
	}
	
	public void renderFromNBT(@Nonnull NBTTagCompound nbt, double x, double y, double z, float partialTicks, @Nullable ResourceLocation destroyStage)
	{
	}
	
	public void renderFromNBTFast(@Nonnull NBTTagCompound nbt, double x, double y, double z, float partialTicks, @Nullable ResourceLocation destroyStage, VertexBuffer buffer)
	{
	}
	
	public void renderTileEntityAt(@Nonnull T te, double x, double y, double z, float partialTicks, @Nullable ResourceLocation destroyStage)
	{
		if(canRenderFromNbt())
			renderFromNBT(getNBTFromTile(te), x, y, z, partialTicks, destroyStage);
	}
	
	public void renderTileEntityFast(@Nonnull T te, double x, double y, double z, float partialTicks, @Nullable ResourceLocation destroyStage, VertexBuffer buffer)
	{
		if(canRenderFromNbt())
			renderFromNBTFast(getNBTFromTile(te), x, y, z, partialTicks, destroyStage, buffer);
	}
	
	/**
	 * useful implementation for rendering an item stack if it can handle
	 * rendering from NBT
	 */
	@Override
	public void renderItem(ItemStack item)
	{
		if(canRenderFromNbt())
		{
			NBTTagCompound nbt = getNBTFromItemStack(item);
			if(nbt != null)
				renderFromNBT(nbt, 0D, 0D, 0D, 0F, null);
		}
	}
	
	public static NBTTagCompound getNBTFromTile(TileEntity tile)
	{
		if(tile instanceof TileSyncable)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			((TileSyncable) tile).writeNBT(nbt);
			return nbt;
		}
		return tile.serializeNBT();
	}
	
	/**
	 * Used when you Ctrl+Select a block and it stores it's NBT data inside of
	 * an ItemStack
	 * 
	 * @return NBT of an ItemStack, or null if there are no tags
	 */
	@Nullable
	public static NBTTagCompound getNBTFromItemStack(ItemStack stack)
	{
		NBTTagCompound tags = stack.getTagCompound();
		if(tags != null)
			tags = tags.getCompoundTag("BlockEntityTag");
		if(tags != null && !tags.hasNoTags())
			return tags.getCompoundTag("tags");
		return null;
	}
	
	protected int getBrightnessForRB(T te, RenderBlocks rb)
	{
		return te != null ? rb.setLighting(te.getWorld(), te.getPos()) : rb.setLighting(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getPosition());
	}
}