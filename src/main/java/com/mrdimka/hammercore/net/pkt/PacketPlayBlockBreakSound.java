package com.mrdimka.hammercore.net.pkt;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.net.packetAPI.IPacketListener;

public class PacketPlayBlockBreakSound implements IPacket, IPacketListener<PacketPlayBlockBreakSound, IPacket>
{
	public String soundName;
	private IBlockState s;
	private float x, y, z, volume, pitch;
	
	public PacketPlayBlockBreakSound()
	{
	}
	
	public PacketPlayBlockBreakSound(IBlockState state, double x, double y, double z, double volume, double pitch)
	{
		s = state;
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
		this.volume = (float) volume;
		this.pitch = (float) pitch;
	}
	
	public PacketPlayBlockBreakSound(String sound, double x, double y, double z, double volume, double pitch)
	{
		soundName = sound;
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
		this.volume = (float) volume;
		this.pitch = (float) pitch;
	}
	
	@Override
	public IPacket onArrived(PacketPlayBlockBreakSound p, MessageContext c)
	{
		if(c.side == Side.CLIENT)
			HammerCore.renderProxy.playSoundAt(HammerCore.audioProxy.getClientPlayer().worldObj, p.s.getBlock().getSoundType().getBreakSound().getRegistryName().toString(), p.x, p.y, p.z, volume, pitch, SoundCategory.BLOCKS);
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setBoolean("HasState", s != null);
		nbt.setInteger("StateID", s == null ? 0 : Block.getStateId(s));
		nbt.setBoolean("HasSound", soundName != null);
		nbt.setString("Sound", soundName != null ? soundName : "");
		nbt.setFloat("X", x);
		nbt.setFloat("Y", y);
		nbt.setFloat("Z", z);
		nbt.setFloat("Vol", volume);
		nbt.setFloat("Pitch", pitch);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		if(nbt.getBoolean("HasState"))
			s = Block.getStateById(nbt.getInteger("StateID"));
		if(nbt.getBoolean("HasSound"))
			soundName = nbt.getString("Sound");
		x = nbt.getFloat("X");
		y = nbt.getFloat("Y");
		z = nbt.getFloat("Z");
		volume = nbt.getFloat("Vol");
		pitch = nbt.getFloat("Pitch");
	}
}