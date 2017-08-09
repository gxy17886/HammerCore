package com.mrdimka.hammercore.asm;

import net.minecraft.launchwrapper.IClassTransformer;

/**
 * Transforms classes
 */
public class HammerCoreTransformer implements IClassTransformer
{
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
//		if(hc_classes == null || name.startsWith("com.mojang")) return basicClass;
//		
//		try
//		{
//			hc_classes.write("[" + classID.toString(16) + "] " + name + " > " + transformedName);
//			hc_classes.newLine();
//			ClassNode cls = ObjectWebUtils.loadClass(basicClass);
//			for(FieldNode f : cls.fields)
//			{
//				Object val = f.value;
//				hc_classes.write("    " + Type.getType(f.desc).getClassName() + " " + f.name + (val != null ? "=" + val : ""));
//				hc_classes.newLine();
//			}
//			hc_classes.flush();
//			classID = classID.add(BigInteger.ONE);
//		}catch(Throwable ioe) {}
		
		return basicClass;
	}
}