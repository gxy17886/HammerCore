package com.pengu.hammercore.client.particle.api.common;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.net.packetAPI.IPacket;
import com.mrdimka.hammercore.net.packetAPI.IPacketListener;
import com.pengu.hammercore.client.particle.api.ParticleList;

public class PacketParticleInfo implements IPacket, IPacketListener<PacketParticleInfo, IPacket>
{
	public NBTTagCompound data;
	
	public PacketParticleInfo(ExtendedParticle particle)
	{
		data = particle.serializeNBT();
		data.setString("Class", particle.getClass().getName());
	}
	
	public PacketParticleInfo()
	{
	}
	
	@Override
	public IPacket onArrived(PacketParticleInfo packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			packet.readClient();
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	private void readClient()
	{
		ExtendedParticle particle = ParticleList.getExtendedParticle(data.getUniqueId("UUID"));
		if(particle == null && Minecraft.getMinecraft().world.provider.getDimension() == data.getInteger("World"))
			try
			{
				particle = (ExtendedParticle) Class.forName(data.getString("Class")).getConstructor(World.class).newInstance(Minecraft.getMinecraft().world);
				ParticleList.spawnExtendedParticle(particle);
			} catch(Throwable err)
			{
			}
		if(particle != null)
			particle.deserializeNBT(data);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("Data", data);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		data = nbt.getCompoundTag("Data");
	}
}