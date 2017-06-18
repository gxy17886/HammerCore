package com.mrdimka.hammercore.proxy;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.client.renderer.IRenderHelper;

public class RenderProxy_Common
{
	public void construct()
	{
		
	}
	
	public void init()
	{
		
	}
	
	public IRenderHelper getRenderHelper()
	{
		return null;
	}
	
	public EntityPlayer getClientPlayer()
	{
		return null;
	}
	
	@Deprecated
	public void playSoundAt(World world, String sound, BlockPos pos, float volume, float pitch, SoundCategory category)
	{
		HammerCore.audioProxy.playSoundAt(world, sound, pos, volume, pitch, category);
	}
	
	@Deprecated
	public void playSoundAt(World world, String sound, double x, double y, double z, float volume, float pitch, SoundCategory category)
	{
		HammerCore.audioProxy.playSoundAt(world, sound, x, y, z, volume, pitch, category);
	}
	
	@Deprecated
	public void playBlockStateBreak(World world, IBlockState type, double x, double y, double z, float volume, float pitch, SoundCategory category)
	{
		HammerCore.audioProxy.playBlockStateBreak(world, type, x, y, z, volume, pitch, category);
	}
	
	public void sendNoSpamMessages(ITextComponent[] messages)
	{
	}
	
	public World getWorld(MessageContext context, int dim)
	{
		if(context == null)
			return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dim);
		if(context.side == Side.SERVER)
			return context.getServerHandler().player.mcServer.getWorld(dim);
		return null;
	}
	
	public World getWorld(MessageContext context)
	{
		if(context == null)
			return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
		if(context.side == Side.SERVER)
			return context.getServerHandler().player.world;
		return null;
	}
	
	public double getBlockReachDistance_client()
	{
		return 0;
	}
	
	public void bindTexture(ResourceLocation texture)
	{
		
	}
}