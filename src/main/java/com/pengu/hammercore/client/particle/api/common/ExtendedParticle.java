package com.pengu.hammercore.client.particle.api.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

import com.mrdimka.hammercore.net.HCNetwork;
import com.pengu.hammercore.net.utils.IPropertyChangeHandler;
import com.pengu.hammercore.net.utils.NetPropertyAbstract;
import com.pengu.hammercore.net.utils.NetPropertyBool;
import com.pengu.hammercore.net.utils.NetPropertyNumber;

public class ExtendedParticle implements ITickable, IPropertyChangeHandler, INBTSerializable<NBTTagCompound>
{
	private final List<NetPropertyAbstract> properties = new ArrayList<>();
	protected World world;
	private UUID uuid;
	private AxisAlignedBB boundingBox;
	protected Random rand;
	
	public final NetPropertyNumber<Double> posX = new NetPropertyNumber<Double>(this);
	public final NetPropertyNumber<Double> posY = new NetPropertyNumber<Double>(this);
	public final NetPropertyNumber<Double> posZ = new NetPropertyNumber<Double>(this);
	public final NetPropertyNumber<Double> motionX = new NetPropertyNumber<Double>(this);
	public final NetPropertyNumber<Double> motionY = new NetPropertyNumber<Double>(this);
	public final NetPropertyNumber<Double> motionZ = new NetPropertyNumber<Double>(this);
	public final NetPropertyNumber<Double> particleGravity = new NetPropertyNumber<Double>(this);
	public final NetPropertyBool isDead = new NetPropertyBool(this);
	public final NetPropertyBool canCollide = new NetPropertyBool(this, true);
	
	public boolean onGround = false;
	public int particleAge, particleAgeMax;
	
	public ExtendedParticle(World world)
	{
		rand = world.rand;
		this.world = world;
		uuid = UUID.randomUUID();
		particleAgeMax = (int) (4F / (rand.nextFloat() * .9F + .1F));
	}
	
	public void setPosition(double x, double y, double z, boolean update)
	{
		posX.syncOnChange = posY.syncOnChange = posZ.syncOnChange = false;
		posX.set(x);
		posY.set(y);
		posZ.set(z);
		posX.syncOnChange = posY.syncOnChange = posZ.syncOnChange = true;
		if(update)
			sendChangesToNearby();
	}
	
	public void setMotion(double x, double y, double z, boolean update)
	{
		motionX.syncOnChange = motionY.syncOnChange = motionZ.syncOnChange = false;
		motionX.set(x);
		motionY.set(y);
		motionZ.set(z);
		motionX.syncOnChange = motionY.syncOnChange = motionZ.syncOnChange = true;
		if(update)
			sendChangesToNearby();
	}
	
	public void setDead()
	{
		isDead.syncOnChange = true;
		isDead.set(true);
	}
	
	@Override
	public void update()
	{
		if(particleAge++ >= particleAgeMax)
		{
			setDead();
			return;
		}
		
		motionY.set(motionY.get() - 0.04D * particleGravity.get());
		move(motionX.get(), motionY.get(), motionZ.get());
		double v = .9800000190734863;
		motionX.set(motionX.get() * v);
		motionY.set(motionY.get() * v);
		motionZ.set(motionZ.get() * v);
		
		if(onGround)
		{
			v = .699999988079071;
			motionX.set(motionX.get() * v);
			motionZ.set(motionZ.get() * v);
		}
		
		if(changes > 0)
		{
			sync();
			changes = 0;
		}
	}
	
	public void sync()
	{
		HCNetwork.getManager("particles").sendToAllAround(new PacketParticleInfo(this), new TargetPoint(world.provider.getDimension(), posX.get(), posY.get(), posZ.get(), 64));
	}
	
	public void spawn()
	{
		ExtendedParticleTicker.spawnParticle(this);
	}
	
	public UUID getUUID()
	{
		return uuid;
	}
	
	@Override
	public int registerProperty(NetPropertyAbstract prop)
	{
		if(properties.contains(prop))
			return properties.indexOf(prop);
		properties.add(prop);
		return properties.size();
	}
	
	@Override
	public void load(int id, NBTTagCompound nbt)
	{
		if(id >= 0 && id < properties.size())
			properties.get(id).readFromNBT(nbt);
	}
	
	private int changes = 0;
	
	public void notifyOfChange(NetPropertyAbstract prop)
	{
		changes++;
	}
	
	@Override
	public void sendChangesToNearby()
	{
		
	}
	
	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		
		nbt.setUniqueId("UUID", uuid);
		
		NBTTagList props = new NBTTagList();
		
		for(NetPropertyAbstract prop : properties)
		{
			NBTTagCompound tag = new NBTTagCompound();
			prop.writeToNBT(tag);
			tag.setString("Class", prop.getClass().getName());
			tag.setInteger("Id", properties.indexOf(prop));
			props.appendTag(tag);
		}
		
		nbt.setTag("Properties", props);
		nbt.setInteger("World", world.provider.getDimension());
		
		return nbt;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		uuid = nbt.getUniqueId("UUID");
		
		NBTTagList props = nbt.getTagList("Properties", NBT.TAG_COMPOUND);
		
		for(int i = 0; i < props.tagCount(); ++i)
		{
			try
			{
				NBTTagCompound tag = props.getCompoundTagAt(i);
				int id = tag.getInteger("Id");
				NetPropertyAbstract prop;
				
				if(properties.size() > id)
				{
					prop = properties.get(id);
					prop.readFromNBT(tag);
				}
			} catch(Throwable err)
			{
			}
		}
	}
	
	public void move(double x, double y, double z)
	{
		double d0 = y;
		
		if(canCollide.get())
		{
			List<AxisAlignedBB> list = this.world.getCollisionBoxes(null, getBoundingBox().addCoord(x, y, z));
			
			for(AxisAlignedBB axisalignedbb : list)
			{
				y = axisalignedbb.calculateYOffset(this.getBoundingBox(), y);
			}
			
			this.setBoundingBox(this.getBoundingBox().offset(0.0D, y, 0.0D));
			
			for(AxisAlignedBB axisalignedbb1 : list)
			{
				x = axisalignedbb1.calculateXOffset(this.getBoundingBox(), x);
			}
			
			this.setBoundingBox(this.getBoundingBox().offset(x, 0.0D, 0.0D));
			
			for(AxisAlignedBB axisalignedbb2 : list)
			{
				z = axisalignedbb2.calculateZOffset(this.getBoundingBox(), z);
			}
			
			this.setBoundingBox(this.getBoundingBox().offset(0.0D, 0.0D, z));
		} else
		{
			this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
		}
		
		this.resetPositionToBB();
		this.onGround = y != y && d0 < 0.0D;
		
		if(x != x)
			motionX.set(0D);
		if(z != z)
			motionZ.set(0D);
	}
	
	protected void resetPositionToBB()
	{
		AxisAlignedBB axisalignedbb = this.getBoundingBox();
		setPosition((axisalignedbb.minX + axisalignedbb.maxX) / 2.0D, axisalignedbb.minY, (axisalignedbb.minZ + axisalignedbb.maxZ) / 2.0D, true);
	}
	
	public AxisAlignedBB getBoundingBox()
	{
		return this.boundingBox;
	}
	
	public void setBoundingBox(AxisAlignedBB bb)
	{
		this.boundingBox = bb;
	}
}