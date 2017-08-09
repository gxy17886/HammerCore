package com.mrdimka.hammercore.structure;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StructureAPI
{
	public static Structure Export(World world, BlockPos start, BlockPos end, boolean includeAir)
	{
		Structure s = new Structure();
		for(int x = Math.min(start.getX(), end.getX()); x <= Math.max(start.getX(), end.getX()); ++x)
			for(int y = Math.min(start.getY(), end.getY()); y <= Math.max(start.getY(), end.getY()); ++y)
				for(int z = Math.min(start.getZ(), end.getZ()); z <= Math.max(start.getZ(), end.getZ()); ++z)
				{
					BlockPos pos = new BlockPos(x, y, z);
					if(world.isAirBlock(pos) && !includeAir)
						continue;
					BlockPos absPos = new BlockPos(x - Math.min(start.getX(), end.getX()), y - Math.min(start.getY(), end.getY()), z - Math.min(start.getZ(), end.getZ()));
					s.placeStateAt(absPos, world.getBlockState(pos));
					
					if(world.getTileEntity(pos) != null)
					{
						NBTTagCompound nbt = world.getTileEntity(pos).serializeNBT();
						nbt.removeTag("x");
						nbt.removeTag("y");
						nbt.removeTag("z");
						s.placeTileNBTAt(absPos, nbt);
					}
				}
		return s;
	}
	
	public static void Import(World world, BlockPos startPos, Structure structure)
	{
		structure.build(world, startPos);
	}
	
	public static void Save(Structure s, OutputStream os) throws IOException
	{
		CompressedStreamTools.writeCompressed(s.serialize(), os);
	}
	
	public static Structure Load(InputStream in) throws IOException
	{
		Structure s = new Structure();
		s.deserialize(CompressedStreamTools.readCompressed(in));
		return s;
	}
}