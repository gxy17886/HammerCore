package com.mrdimka.hammercore.api.dynlight;

import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;

public class DynLightContainer
{
	private final IDynlightSrc src;
	private int x, y, z, prevX, prevY, prevZ;
	
	public DynLightContainer(IDynlightSrc src)
	{
		this.src = src;
	}
	
	public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
    
    public IDynlightSrc getLightSource()
    {
    	return src;
    }
    
    public boolean update()
    {
    	IMovable mov = src.getSrcInfo();
    	
    	if(mov == null || !mov.isAlive()) return false;
    	
    	if(hasSrcMoved(mov))
    	{
    		mov.getWorld().checkLightFor(EnumSkyBlock.BLOCK, new BlockPos(x, y, z));
    		mov.getWorld().checkLightFor(EnumSkyBlock.BLOCK, new BlockPos(prevX, prevY, prevZ));
    	}
    	
    	return true;
    }
    
    private boolean hasSrcMoved(IMovable mov)
    {
    	int nx = mov.getX();
    	int ny = mov.getY();
    	int nz = mov.getZ();
    	
    	if(x != nx || y != ny || z != nz)
    	{
    		prevX = x;
    		prevY = y;
    		prevZ = z;
    		x = nx;
    		y = ny;
    		z = nz;
    		
    		return true;
    	}
    	
    	return false;
    }
}