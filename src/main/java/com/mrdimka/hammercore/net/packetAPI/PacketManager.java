package com.mrdimka.hammercore.net.packetAPI;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketManager
{
	private static final Map<String, PacketManager> managers = new HashMap<String, PacketManager>();
	
	final Map<Class<? extends IPacket>, IPacketListener<?, ?>> registry = new HashMap<Class<? extends IPacket>, IPacketListener<?, ?>>();
	final Map<String, IPacketListener<?, ?>> stringClassRegistry = new HashMap<String, IPacketListener<?, ?>>();
	private final SimpleNetworkWrapper wrapper;
	final String channel;
	
	/**
	 * Creates a new {@link PacketManager} with passed string as a channel ID or
	 * name. <br>
	 * MUST be constructed under FML initialization event, if you want it to
	 * work properly.
	 * 
	 * @param channel
	 *            A channel that this manager is working on.
	 */
	public PacketManager(String channel)
	{
		if(getManagerByChannel(channel) != null)
			throw new RuntimeException("Duplicate channel ID for " + channel + " (" + this + ") and (" + getManagerByChannel(channel) + ")!!!");
		managers.put(channel, this);
		this.channel = channel;
		this.wrapper = NetworkRegistry.INSTANCE.newSimpleChannel("hammercore" + channel);
		this.wrapper.registerMessage(PacketCustomNBT.class, PacketCustomNBT.class, 1, Side.CLIENT);
		this.wrapper.registerMessage(PacketCustomNBT.class, PacketCustomNBT.class, 1, Side.SERVER);
	}
	
	/**
	 * Gets a channel for {@link PacketManager}.
	 * 
	 * @return A {@link String} representation of channel.
	 */
	public String getChannel()
	{
		return channel;
	}
	
	/**
	 * Returns packet manager for passed channel, or null.
	 * 
	 * @param channel
	 *            A channel to lookup with.
	 * @return A {@link PacketManager} or null, if not exists for passed
	 *         channel.
	 */
	public static PacketManager getManagerByChannel(String channel)
	{
		return managers.get(channel);
	}
	
	/**
	 * Register a Packet listener for specified IPacket class.
	 * 
	 * @param packet
	 *            The packet class to add listener for
	 * @param listener
	 *            The listener instance that will listen for packet events
	 */
	public <PKT extends IPacket, REPLY extends IPacket> void registerPacket(Class<PKT> packet, IPacketListener<PKT, REPLY> listener)
	{
		registry.put(packet, listener);
		stringClassRegistry.put(packet.getName(), listener);
	}
	
	public <PKT extends IPacket> IPacketListener<PKT, ?> getListener(Class<PKT> packet)
	{
		return (IPacketListener<PKT, ?>) stringClassRegistry.get(packet.getName());
	}
	
	/**
	 * Send this packet to everyone. The {@link IPacketListener} for this packet
	 * type should be on the CLIENT side.
	 *
	 * @param packet
	 *            The packet to send
	 */
	public void sendToAll(IPacket packet)
	{
		wrapper.sendToAll(new PacketCustomNBT(packet, channel));
	}
	
	/**
	 * Send this packet to the specified player. The {@link IPacketListener} for
	 * this packet type should be on the CLIENT side.
	 *
	 * @param packet
	 *            The packet to send
	 * @param player
	 *            The player to send it to
	 */
	public void sendTo(IPacket packet, EntityPlayerMP player)
	{
		wrapper.sendTo(new PacketCustomNBT(packet, channel), player);
	}
	
	/**
	 * Send this packet to everyone within a certain range of a point. The
	 * {@link IPacketListener} for this packet type should be on the CLIENT
	 * side.
	 *
	 * @param packet
	 *            The packet to send
	 * @param point
	 *            The {@link TargetPoint} around which to send
	 */
	public void sendToAllAround(IPacket packet, TargetPoint point)
	{
		wrapper.sendToAllAround(new PacketCustomNBT(packet, channel), point);
	}
	
	/**
	 * Send this packet to everyone within the supplied dimension. The
	 * {@link IPacketListener} for this packet type should be on the CLIENT
	 * side.
	 *
	 * @param packet
	 *            The packet to send
	 * @param dimensionId
	 *            The dimension id to target
	 */
	public void sendToDimension(IPacket packet, int dimensionId)
	{
		wrapper.sendToDimension(new PacketCustomNBT(packet, channel), dimensionId);
	}
	
	/**
	 * Send this packet to the server. The {@link IPacketListener} for this
	 * packet type should be on the SERVER side.
	 *
	 * @param packet
	 *            The packet to send
	 */
	public void sendToServer(IPacket packet)
	{
		wrapper.sendToServer(new PacketCustomNBT(packet, channel));
	}
}