package com.pengu.hammercore.client.particle.def;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

import com.mrdimka.hammercore.api.IUpdatable;
import com.mrdimka.hammercore.vec.Vector3;

public class LightningBoltCommon implements IUpdatable
{
	ArrayList<Segment> segments = new ArrayList<>();
	Vector3 start;
	Vector3 end;
	HashMap splitparents = new HashMap();
	public float multiplier;
	public float length;
	public int numsegments0;
	public int increment;
	public int type = 0;
	public boolean nonLethal = false;
	private int numsplits;
	private boolean finalized;
	private boolean canhittarget = true;
	private Random rand;
	public long seed;
	public int particleAge;
	public int particleMaxAge;
	private AxisAlignedBB boundingBox;
	private World world;
	public EntityLivingBase wrapper;
	public static final float speed = 3.0f;
	public static final int fadetime = 20;
	public static int damage;
	
	public LightningBoltCommon(World world, Vector3 jammervec, Vector3 targetvec, long seed)
	{
		this.world = world;
		start = jammervec;
		end = targetvec;
		this.seed = seed;
		rand = new Random(seed);
		numsegments0 = 1;
		increment = 1;
		length = end.copy().sub(start).length();
		particleMaxAge = 3 + rand.nextInt(3) - 1;
		multiplier = 1.0f;
		particleAge = -(int) (length * 3.0f);
		boundingBox = new AxisAlignedBB((double) Math.min(start.x, end.x), (double) Math.min(start.y, end.y), (double) Math.min(start.z, end.z), (double) Math.max(start.x, end.x), (double) Math.max(start.y, end.y), (double) Math.max(start.z, end.z)).expand((double) (length / 2.0f), (double) (length / 2.0f), (double) (length / 2.0f));
		segments.add(new Segment(start, end));
	}
	
	public LightningBoltCommon(World world, Entity detonator, Entity target, long seed)
	{
		this(world, new Vector3(detonator), new Vector3(target), seed);
	}
	
	public LightningBoltCommon(World world, Entity detonator, Entity target, long seed, int speed)
	{
		this(world, new Vector3(detonator), new Vector3(target.posX, target.posY + (double) target.getEyeHeight() - 0.699999988079071, target.posZ), seed);
		increment = speed;
		multiplier = 0.4f;
	}
	
	public LightningBoltCommon(World world, TileEntity detonator, Entity target, long seed)
	{
		this(world, new Vector3(detonator), new Vector3(target), seed);
	}
	
	public LightningBoltCommon(World world, TileEntity detonator, double x, double y, double z, long seed)
	{
		this(world, new Vector3(detonator), new Vector3(x, y, z), seed);
	}
	
	public LightningBoltCommon(World world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration, float multi)
	{
		this(world, new Vector3(x1, y1, z1), new Vector3(x, y, z), seed);
		particleMaxAge = duration + rand.nextInt(duration) - duration / 2;
		multiplier = multi;
	}
	
	public LightningBoltCommon(World world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration, float multi, int speed)
	{
		this(world, new Vector3(x1, y1, z1), new Vector3(x, y, z), seed);
		particleMaxAge = duration + rand.nextInt(duration) - duration / 2;
		multiplier = multi;
		increment = speed;
	}
	
	public void setWrapper(EntityLivingBase entity)
	{
		wrapper = entity;
	}
	
	public void setMultiplier(float m)
	{
		multiplier = m;
	}
	
	public void fractal(int splits, float amount, float splitchance, float splitlength, float splitangle)
	{
		if(finalized)
		{
			return;
		}
		ArrayList<Segment> oldsegments = segments;
		segments = new ArrayList();
		Segment prev = null;
		for(Segment segment : oldsegments)
		{
			int i;
			prev = segment.prev;
			Vector3 subsegment = segment.diff.copy().scale(1.0f / (float) splits);
			BoltPoint[] newpoints = new BoltPoint[splits + 1];
			Vector3 startpoint = segment.startpoint.point;
			newpoints[0] = segment.startpoint;
			newpoints[splits] = segment.endpoint;
			for(i = 1; i < splits; ++i)
			{
				Vector3 randoff = Vector3.getPerpendicular(segment.diff).rotate(rand.nextFloat() * 360.0f, segment.diff);
				randoff.scale((rand.nextFloat() - 0.5f) * amount);
				Vector3 basepoint = startpoint.copy().add(subsegment.copy().scale(i));
				newpoints[i] = new BoltPoint(basepoint, randoff);
			}
			for(i = 0; i < splits; ++i)
			{
				Segment next = new Segment(newpoints[i], newpoints[i + 1], segment.light, segment.segmentno * splits + i, segment.splitno);
				next.prev = prev;
				if(prev != null)
				{
					prev.next = next;
				}
				if(i != 0 && rand.nextFloat() < splitchance)
				{
					Vector3 splitrot = Vector3.xCrossProduct(next.diff).rotate(rand.nextFloat() * 360.0f, next.diff);
					Vector3 diff = next.diff.copy().rotate((rand.nextFloat() * 0.66f + 0.33f) * splitangle, splitrot).scale(splitlength);
					++numsplits;
					splitparents.put(numsplits, next.splitno);
					Segment split = new Segment(newpoints[i], new BoltPoint(newpoints[i + 1].basepoint, newpoints[i + 1].offsetvec.copy().add(diff)), segment.light / 2.0f, next.segmentno, numsplits);
					split.prev = prev;
					segments.add(split);
				}
				prev = next;
				segments.add(next);
			}
			if(segment.next == null)
				continue;
			segment.next.prev = prev;
		}
		numsegments0 *= splits;
	}
	
	public void defaultFractal()
	{
		fractal(2, length * multiplier / 8.0f, 0.7f, 0.1f, 45.0f);
		fractal(2, length * multiplier / 12.0f, 0.5f, 0.1f, 50.0f);
		fractal(2, length * multiplier / 17.0f, 0.5f, 0.1f, 55.0f);
		fractal(2, length * multiplier / 23.0f, 0.5f, 0.1f, 60.0f);
		fractal(2, length * multiplier / 30.0f, 0.0f, 0.0f, 0.0f);
		fractal(2, length * multiplier / 34.0f, 0.0f, 0.0f, 0.0f);
		fractal(2, length * multiplier / 40.0f, 0.0f, 0.0f, 0.0f);
	}
	
	private void vecBBDamageSegment(Vector3 start, Vector3 end, ArrayList<Entity> entitylist)
	{
		Vec3d start3D = start.toVec3d();
		Vec3d end3D = end.toVec3d();
		try
		{
			for(Entity entity : entitylist)
			{
				AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expandXyz((double) entity.getCollisionBorderSize());
				if(!(entity instanceof EntityLiving) || !axisalignedbb.isVecInside(start3D))
					continue;
				
				if(wrapper == null || !(wrapper instanceof EntityLiving))
				{
					switch(type)
					{
					case 0:
					{
						entity.attackEntityFrom(DamageSource.MAGIC, damage);
						poisonBolt(entity, MobEffects.NAUSEA, 2);
						break;
					}
					case 1:
					{
						entity.attackEntityFrom(DamageSource.MAGIC, damage);
						break;
					}
					case 2:
					{
						entity.attackEntityFrom(DamageSource.MAGIC, damage);
						poisonBolt(entity, MobEffects.SLOWNESS, 2);
						break;
					}
					case 3:
					{
						entity.attackEntityFrom(DamageSource.MAGIC, damage);
						poisonBolt(entity, MobEffects.POISON, 1);
						break;
					}
					case 4:
					{
						entity.attackEntityFrom(DamageSource.MAGIC, damage);
						entity.setFire(2);
						break;
					}
					case 5:
					{
						entity.attackEntityFrom(DamageSource.MAGIC, damage);
						poisonBolt(entity, MobEffects.BLINDNESS, 1);
					}
					}
					continue;
				}
				
				switch(type)
				{
				case 0:
				{
					entity.attackEntityFrom(DamageSource.causeMobDamage(wrapper), damage);
					poisonBolt(entity, MobEffects.NAUSEA, 2);
					break;
				}
				case 1:
				{
					entity.attackEntityFrom(DamageSource.causeMobDamage(wrapper), damage);
					break;
				}
				case 2:
				{
					entity.attackEntityFrom(DamageSource.causeMobDamage(wrapper), damage);
					poisonBolt(entity, MobEffects.SLOWNESS, 2);
					break;
				}
				case 3:
				{
					entity.attackEntityFrom(DamageSource.causeMobDamage(wrapper), damage);
					poisonBolt(entity, MobEffects.POISON, 1);
					break;
				}
				case 4:
				{
					entity.attackEntityFrom(DamageSource.causeMobDamage(wrapper), damage);
					entity.setFire(2);
					break;
				}
				case 5:
				{
					entity.attackEntityFrom(DamageSource.causeMobDamage(wrapper), damage);
					poisonBolt(entity, MobEffects.BLINDNESS, 1);
				}
				}
			}
		} catch(Exception e)
		{
			// empty catch block
		}
	}
	
	private void poisonBolt(Entity entity, Potion poison, int durmod)
	{
		int byte0 = 0;
		
		if(world.getDifficulty() != EnumDifficulty.PEACEFUL)
		{
			if(world.getDifficulty() == EnumDifficulty.EASY)
				byte0 = 1;
			else if(world.getDifficulty() == EnumDifficulty.NORMAL)
				byte0 = 3;
			else if(world.getDifficulty() == EnumDifficulty.HARD)
				byte0 = 6;
		}
		
		if(byte0 > 0 && entity instanceof EntityLivingBase)
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(poison, byte0 * 20 * durmod, 0));
	}
	
	private void bbTestEntityDamage()
	{
		if(nonLethal)
			return;
		List<Entity> nearentities = world.getEntitiesWithinAABB(Entity.class, boundingBox);
		if(nearentities.size() == 0)
			return;
		for(Segment segment : segments)
			vecBBDamageSegment(segment.startpoint.point, segment.endpoint.point, (ArrayList) nearentities);
	}
	
	private float rayTraceResistance(Vector3 start, Vector3 end, float prevresistance)
	{
		RayTraceResult mop = world.rayTraceBlocks(start.toVec3d(), end.toVec3d());
		if(mop == null)
			return prevresistance;
		
		if(mop.typeOfHit == Type.BLOCK)
		{
			IBlockState state = world.getBlockState(mop.getBlockPos());
			return prevresistance + state.getBlockHardness(world, mop.getBlockPos());
		}
		return prevresistance;
	}
	
	private void calculateCollisionAndDiffs()
	{
		HashMap<Integer, Integer> lastactivesegment = new HashMap<Integer, Integer>();
		Collections.sort(segments, new SegmentSorter());
		int lastsplitcalc = 0;
		int lastactiveseg = 0;
		float splitresistance = 0.0f;
		for(Segment segment : segments)
		{
			if(segment.splitno > lastsplitcalc)
			{
				lastactivesegment.put(lastsplitcalc, lastactiveseg);
				lastsplitcalc = segment.splitno;
				lastactiveseg = (Integer) lastactivesegment.get(splitparents.get(segment.splitno));
				float f = splitresistance = lastactiveseg >= segment.segmentno ? 0.0f : 50.0f;
			}
			if(splitresistance >= 40.0f * segment.light)
				continue;
			lastactiveseg = segment.segmentno;
		}
		lastactivesegment.put(lastsplitcalc, lastactiveseg);
		lastsplitcalc = 0;
		lastactiveseg = (Integer) lastactivesegment.get(0);
		Iterator iterator = segments.iterator();
		while(iterator.hasNext())
		{
			Segment segment2 = (Segment) iterator.next();
			if(lastsplitcalc != segment2.splitno)
			{
				lastsplitcalc = segment2.splitno;
				lastactiveseg = (Integer) lastactivesegment.get(segment2.splitno);
			}
			if(segment2.segmentno > lastactiveseg)
			{
				iterator.remove();
			}
			segment2.calcEndDiffs();
		}
		if((Integer) lastactivesegment.get(0) + 1 < numsegments0)
		{
			canhittarget = false;
		}
	}
	
	public void finalizeBolt()
	{
		if(finalized)
		{
			return;
		}
		finalized = true;
		calculateCollisionAndDiffs();
		Collections.sort(segments, new SegmentLightSorter());
	}
	
	public void onUpdate()
	{
		particleAge += increment;
		if(particleAge > particleMaxAge)
			particleAge = particleMaxAge;
		bbTestEntityDamage();
	}
	
	public class SegmentSorter implements Comparator
	{
		final LightningBoltCommon this$0;
		
		public int compare(Segment o1, Segment o2)
		{
			int comp = Integer.valueOf(o1.splitno).compareTo(o2.splitno);
			if(comp == 0)
			{
				return Integer.valueOf(o1.segmentno).compareTo(o2.segmentno);
			}
			return comp;
		}
		
		public int compare(Object obj, Object obj1)
		{
			return compare((Segment) obj, (Segment) obj1);
		}
		
		public SegmentSorter()
		{
			this$0 = LightningBoltCommon.this;
		}
	}
	
	public class SegmentLightSorter implements Comparator
	{
		final LightningBoltCommon this$0;
		
		public int compare(Segment o1, Segment o2)
		{
			return Float.compare(o2.light, o1.light);
		}
		
		public int compare(Object obj, Object obj1)
		{
			return compare((Segment) obj, (Segment) obj1);
		}
		
		public SegmentLightSorter()
		{
			this$0 = LightningBoltCommon.this;
		}
	}
	
	public class Segment
	{
		public BoltPoint startpoint;
		public BoltPoint endpoint;
		public Vector3 diff;
		public Segment prev;
		public Segment next;
		public Vector3 nextdiff;
		public Vector3 prevdiff;
		public float sinprev;
		public float sinnext;
		public float light;
		public int segmentno;
		public int splitno;
		final LightningBoltCommon this$0;
		
		public void calcDiff()
		{
			diff = endpoint.point.copy().sub(startpoint.point);
		}
		
		public void calcEndDiffs()
		{
			Vector3 thisdiffnorm;
			if(prev != null)
			{
				Vector3 prevdiffnorm = prev.diff.copy().normalize();
				thisdiffnorm = diff.copy().normalize();
				prevdiff = thisdiffnorm.add(prevdiffnorm).normalize();
				sinprev = (float) Math.sin(Vector3.anglePreNorm(thisdiffnorm, prevdiffnorm.scale(-1.0f)) / 2.0f);
			} else
			{
				prevdiff = diff.copy().normalize();
				sinprev = 1.0f;
			}
			if(next != null)
			{
				Vector3 nextdiffnorm = next.diff.copy().normalize();
				thisdiffnorm = diff.copy().normalize();
				nextdiff = thisdiffnorm.add(nextdiffnorm).normalize();
				sinnext = (float) Math.sin(Vector3.anglePreNorm(thisdiffnorm, nextdiffnorm.scale(-1.0f)) / 2.0f);
			} else
			{
				nextdiff = diff.copy().normalize();
				sinnext = 1.0f;
			}
		}
		
		public String toString()
		{
			return String.valueOf(startpoint.point.toString()) + " " + endpoint.point.toString();
		}
		
		public Segment(BoltPoint start, BoltPoint end, float light, int segmentnumber, int splitnumber)
		{
			this$0 = LightningBoltCommon.this;
			startpoint = start;
			endpoint = end;
			this.light = light;
			segmentno = segmentnumber;
			splitno = splitnumber;
			calcDiff();
		}
		
		public Segment(Vector3 start, Vector3 end)
		{
			this(new BoltPoint(start, new Vector3(0.0, 0.0, 0.0)), new BoltPoint(end, new Vector3(0.0, 0.0, 0.0)), 1.0f, 0, 0);
		}
	}
	
	public class BoltPoint
	{
		Vector3 point;
		Vector3 basepoint;
		Vector3 offsetvec;
		final LightningBoltCommon this$0;
		
		public BoltPoint(Vector3 basepoint, Vector3 offsetvec)
		{
			
			this$0 = LightningBoltCommon.this;
			point = basepoint.copy().add(offsetvec);
			this.basepoint = basepoint;
			this.offsetvec = offsetvec;
		}
	}
	
	boolean spawned = false;
	@Override
	public void update()
	{
		onUpdate();
		if(!spawned)
		{
			spawned = true;
		}
	}
	
	@Override
	public boolean isAlive()
	{
		return particleAge < particleMaxAge;
	}
}