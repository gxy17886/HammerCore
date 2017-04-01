package com.mrdimka.hammercore.api.multipart;

import java.lang.reflect.Constructor;
import java.security.SecureRandom;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.api.handlers.IHandlerProvider;
import com.mrdimka.hammercore.common.InterItemStack;
import com.mrdimka.hammercore.common.blocks.multipart.TileMultipart;
import com.mrdimka.hammercore.common.utils.WorldUtil;

/**
 * The main part of {@link MultipartAPI}
 */
public abstract class MultipartSignature
{
	protected static final SecureRandom RANDOM = new SecureRandom();
	
	/** Set this to true to force multipart re-render every multipart signature onto a buffer */
	protected boolean reRenderRequired = false;
	protected TileMultipart owner;
	protected World world;
	protected BlockPos pos;
	protected IBlockState state;
	
	public final void setOwner(TileMultipart owner)
	{
		this.owner = owner;
	}
	
	public final TileMultipart getOwner()
	{
		return owner;
	}
	
	public final void requestSync()
	{
		if(owner != null) owner.sync();
	}
	
	public final IBlockState getState()
	{
		return state;
	}
	
	public final void setState(IBlockState state)
	{
		this.state = state;
	}
	
	public final void setWorld(World worldIn)
	{
		world = worldIn;
	}
	
	public final void setPos(BlockPos posIn)
	{
		pos = posIn;
	}
	
	public ItemStack getPickBlock(EntityPlayer player)
	{
		return InterItemStack.NULL_STACK;
	}
	
	public SoundType getSoundType(EntityPlayer player)
	{
		if(getState() != null) return getState().getBlock().getSoundType(getState(), world, pos, player);
		return SoundType.STONE;
	}
	
	protected float getMultipartHardness(EntityPlayer player)
	{
		return getState() != null ? getState().getBlockHardness(world, pos) : 0F;
	}
	
	public float getHardness(EntityPlayer player)
	{
		float hardness = getMultipartHardness(player);
        if(hardness < 0F || getState() == null) return 0F;
        if(!ForgeHooks.canHarvestBlock(getState().getBlock(), player, world, pos)) return player.getDigSpeed(getState(), pos) / hardness / 100F;
        else return player.getDigSpeed(getState(), pos) / hardness / 30F;
	}
	
	public IHandlerProvider getProvider(EnumFacing toFace)
	{
		IHandlerProvider provider = owner;
		if(world != null && world.isBlockLoaded(pos.offset(toFace)))
		{
			provider = WorldUtil.cast(world, IHandlerProvider.class);
		}
		return provider != null ? provider : owner;
	}
	
	public int getWeakPower(EnumFacing side)
	{
		return 0;
	}
	
	public int getStrongPower(EnumFacing side)
	{
		return 0;
	}

    /**
     * Spawn a digging particle effect in the world, this is a wrapper
     * around EffectRenderer.addBlockHitEffects to allow the multipart more
     * control over the particles. Useful when you have entirely different
     * texture sheets for different sides/locations in the world.
     *
     * @param worldObj The current world
     * @param target The target the player is looking at {x/y/z/side/sub}
     * @param manager A reference to the current particle manager.
     */
    @SideOnly(Side.CLIENT)
    public void addHitEffects(World worldObj, RayTraceResult target, net.minecraft.client.particle.ParticleManager manager)
    {
    	IBlockState state = getState();
    	
        if(state != null && state.getRenderType() != EnumBlockRenderType.INVISIBLE)
        {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            float f = 0.1F;
            AxisAlignedBB axisalignedbb = getBoundingBox();
            double d0 = i + RANDOM.nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minX;
            double d1 = j + RANDOM.nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minY;
            double d2 = k + RANDOM.nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - 0.20000000298023224D) + 0.10000000149011612D + axisalignedbb.minZ;
            
            EnumFacing side = target.sideHit;
            
            if(side == EnumFacing.DOWN)
            {
                d1 = (double)j + axisalignedbb.minY - 0.10000000149011612D;
            }
            
            if(side == EnumFacing.UP)
            {
                d1 = (double)j + axisalignedbb.maxY + 0.10000000149011612D;
            }
            
            if (side == EnumFacing.NORTH)
            {
                d2 = (double)k + axisalignedbb.minZ - 0.10000000149011612D;
            }
            
            if (side == EnumFacing.SOUTH)
            {
                d2 = (double)k + axisalignedbb.maxZ + 0.10000000149011612D;
            }
            
            if (side == EnumFacing.WEST)
            {
                d0 = (double)i + axisalignedbb.minX - 0.10000000149011612D;
            }
            
            if (side == EnumFacing.EAST)
            {
                d0 = (double)i + axisalignedbb.maxX + 0.10000000149011612D;
            }
            
            try
            {
            	Constructor<ParticleDigging> digging = ParticleDigging.class.getConstructor(World.class, double.class, double.class, double.class, double.class, double.class, double.class, IBlockState.class);
                digging.setAccessible(true);
                ParticleDigging d = digging.newInstance(worldObj, d0, d1, d2, 0.0D, 0.0D, 0.0D, state);
                Particle p = d.setBlockPos(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F);
                manager.addEffect(p);
            }
            catch(Throwable err) {} //Ingore anything bad - we don't want any of that!
        }
    }

    /**
     * Spawn particles for when the multipart is destroyed. Due to the nature
     * of how this is invoked, the x/y/z locations are not always guaranteed
     * to host your block. So be sure to do proper sanity checks before assuming
     * that the location is this block.
     *
     * @param world The current world
     * @param pos Position to spawn the particle
     * @param manager A reference to the current particle manager.
     */
    @SideOnly(Side.CLIENT)
    public void addDestroyEffects(World world, BlockPos pos, net.minecraft.client.particle.ParticleManager manager)
    {
        int i = 4;
        
        if(state != null) for (int j = 0; j < i; ++j)
        {
            for (int k = 0; k < i; ++k)
            {
                for (int l = 0; l < i; ++l)
                {
                    double d0 = ((double)j + .5D) / 4D;
                    double d1 = ((double)k + .5D) / 4D;
                    double d2 = ((double)l + .5D) / 4D;
                    
                    try
                    {
                    	Constructor<ParticleDigging> digging = ParticleDigging.class.getConstructor(World.class, double.class, double.class, double.class, double.class, double.class, double.class, IBlockState.class);
                        digging.setAccessible(true);
                        ParticleDigging d = digging.newInstance(world, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, d0 - 0.5D, d1 - 0.5D, d2 - 0.5D, getState());
                        manager.addEffect(d.setBlockPos(pos));
                    }
                    catch(Throwable err) {} //Ingore anything bad - we don't want any of that!
                }
            }
        }
    }
	
	/**
	 * Determines if multi-part signature can be placed onto a tile.
	 */
	public boolean canPlaceInto(TileMultipart tmp)
	{
		return tmp != null && tmp.canPlace_def(this);
	}
	
	/**
     * Determine if this block can make a redstone connection on the side provided,
     * Useful to control which sides are inputs and outputs for redstone wires.
     *
     * @param side The side that is trying to make the connection, CAN BE NULL
     * @return True to make the connection
     */
    public boolean canConnectRedstone(EnumFacing side)
    {
        return false;
    }
	
	public boolean onSignatureActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return false;
	}
	
	public int getLightLevel()
	{
		return 0;
	}
	
	public void writeToNBT(NBTTagCompound nbt) {}
	public void readFromNBT(NBTTagCompound nbt) {}
	
	public abstract AxisAlignedBB getBoundingBox();
	
	public static MultipartSignature createAndLoadSignature(NBTTagCompound nbt)
	{
		try
		{
			MultipartSignature signature = (MultipartSignature) Class.forName(nbt.getString("class")).newInstance();
			signature.readSignature(nbt);
			return signature;
		} catch(Throwable err) { HammerCore.LOG.error("Failed to load signature for " + nbt.getString("class") + "! This is a bug!"); err.printStackTrace(); }
		return null;
	}
	
	public final void writeSignature(NBTTagCompound nbt)
	{
		nbt.setString("class", getClass().getName());
		if(state != null)
		{
			nbt.setString("block", state.getBlock().getRegistryName().toString());
			nbt.setInteger("meta", state.getBlock().getMetaFromState(state));
		}
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		nbt.setTag("nbt", tag);
	}
	
	public final void readSignature(NBTTagCompound nbt)
	{
		if(nbt.hasKey("block", NBT.TAG_STRING) && nbt.hasKey("meta", NBT.TAG_INT)) state = GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(nbt.getString("block"))).getStateFromMeta(nbt.getInteger("meta"));
		pos = new BlockPos(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z"));
		readFromNBT(nbt.getCompoundTag("nbt"));
	}
	
	/** Drop all things in here! */
	public void onRemoved(boolean spawnDrop)
	{
		if(spawnDrop && !world.isRemote && state != null)
			for(ItemStack stack : state.getBlock().getDrops(world, pos, state, 0))
				WorldUtil.spawnItemStack(world, pos, stack);
	}
	
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		
	}
}