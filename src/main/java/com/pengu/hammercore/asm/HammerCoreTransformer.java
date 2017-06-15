package com.pengu.hammercore.asm;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

/**
 * Transforms classes
 */
public class HammerCoreTransformer implements IClassTransformer
{
	/* net/minecraft/world/World */
	private String classNameWorld = "ajs";
	
	/* (Lnet/minecraft/util/BlockPos;Lnet/minecraft/world/EnumSkyBlock;)I /
	 * func_175638_a */
	private String targetMethodDesc = "(Lco;Lajw;)I";
	
	/* net/minecraft/world/World.getRawLight / func_175638_a */
	private String computeLightValueMethodName = "a";
	
	/* (Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;
	 * Lnet/minecraft/util/BlockPos;)I */
	private String goalInvokeDesc = "(Latl;Laju;Lco;)I";
	
	// private static final RoundRobinList<SaveThread> saves = new
	// RoundRobinList<>();
	//
	// static
	// {
	// for(int i = 0; i < 2; ++i)
	// {
	// SaveThread e = new SaveThread();
	// e.start();
	// saves.add(e);
	// }
	// }
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		byte[] cl = handleTransform(name, transformedName, basicClass);
		return cl;
	}
	
	private byte[] handleTransform(String name, String transformedName, byte[] basicClass)
	{
		if(name.equals(classNameWorld))
			return handleWorldTransform(basicClass, true);
		else if(name.equals("net.minecraft.world.World"))
		{
			computeLightValueMethodName = "getRawLight";
			goalInvokeDesc = "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)I";
			return handleWorldTransform(basicClass, false);
		}
		
		if(name.equals("net.minecraft.block.BlockSnow") || name.equals("aqs"))
		{
			HammerCoreCore.ASM_LOG.info("Transforming net.minecraft.block.BlockSnow (" + name + ")...");
			ClassNode classNode = ObjectWebUtils.loadClass(basicClass);
			boolean obf = name.equals("aqs");
			HammerCoreCore.ASM_LOG.info("-We are in " + (obf ? "" : "de") + "obfuscated minecraft.");
			
			String desc = "(Lajs;Lco;Latl;Ljava/util/Random;)V";
			if(!obf)
				desc = "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Ljava/util/Random;)V";
			
			for(MethodNode m : classNode.methods)
			{
				if(m.desc.equals(desc) && (m.name.equals("b") || m.name.equals("func_180650_b") || m.name.equals("updateTick")))
				{
					InsnList updateTick = new InsnList();
					updateTick.add(new VarInsnNode(Opcodes.ALOAD, 1));
					updateTick.add(new VarInsnNode(Opcodes.ALOAD, 2));
					updateTick.add(new VarInsnNode(Opcodes.ALOAD, 3));
					updateTick.add(new VarInsnNode(Opcodes.ALOAD, 4));
					updateTick.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/pengu/hammercore/asm/SnowfallHooks", "updateTick", m.desc));
					updateTick.add(new InsnNode(Opcodes.RETURN));
					
					m.instructions = updateTick;
					HammerCoreCore.ASM_LOG.info("-Sending instructions to BlockSnow for function updateTick");
				}
			}
			
			return ObjectWebUtils.writeClassToByteArray(classNode);
		}
		
		return basicClass;
	}
	
	private String insnToString(AbstractInsnNode insn)
	{
		insn.accept(mp);
		StringWriter sw = new StringWriter();
		printer.print(new PrintWriter(sw));
		printer.getText().clear();
		return sw.toString();
	}
	
	private Printer printer = new Textifier();
	private TraceMethodVisitor mp = new TraceMethodVisitor(printer);
	
	private byte[] handleWorldTransform(byte[] bytes, boolean obf)
	{
		HammerCoreCore.ASM_LOG.info("Transforming net.minecraft.world.World...");
		// System.out.println("**************** Dynamic Lights transform running on World, obf: "
		// + obf + " *********************** ");
		ClassNode classNode = ObjectWebUtils.loadClass(bytes);
		
		String desc = "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Z)Z";
		if(obf)
			desc = "(L" + classNameWorld + ";Lco;Z)Z";
		
		InsnList canSnowAtBody = new InsnList();
		canSnowAtBody.add(new VarInsnNode(Opcodes.ALOAD, 0));
		canSnowAtBody.add(new VarInsnNode(Opcodes.ALOAD, 1));
		canSnowAtBody.add(new VarInsnNode(Opcodes.ILOAD, 2));
		canSnowAtBody.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/pengu/hammercore/asm/SnowfallHooks", "canSnowAtBody", desc));
		canSnowAtBody.add(new InsnNode(Opcodes.IRETURN));
		
//		boolean add_func_72853_d = true;
		
		for(MethodNode m : classNode.methods)
		{
			if(m.name.equals("canSnowAtBody"))
			{
				m.instructions = canSnowAtBody;
				HammerCoreCore.ASM_LOG.info("Sending instructions to World for function canSnowAtBody");
			}
			
//			if((m.name.equals("getMoonPhase") || m.name.equals("func_72853_d") || m.name.equals("D")) && m.desc.equals("()I"))
//			{
//				add_func_72853_d = false;
//				HammerCoreCore.ASM_LOG.info("Sending instructions to World for function getMoonPhase");
//				AnnotationNode sideonly = null;
//				for(AnnotationNode node : m.visibleAnnotations)
//					if(node.desc.equals("Lnet/minecraftforge/fml/relauncher/SideOnly;"))
//					{
//						sideonly = node;
//						break;
//					}
//			}
			
			if(m.name.equals(computeLightValueMethodName) && (!obf || m.desc.equals(targetMethodDesc)))
			{
				// System.out.println("In target method " +
				// computeLightValueMethodName + ":" + m.desc + ", Patching!");
				AbstractInsnNode targetNode = null;
				Iterator<AbstractInsnNode> iter = m.instructions.iterator();
				boolean found = false;
				int index = 0;
				while(iter.hasNext())
				{
					targetNode = iter.next();
					if(targetNode.getOpcode() == Opcodes.ASTORE)
					{
						VarInsnNode astore = (VarInsnNode) targetNode;
						// System.out.println("Found ASTORE Node at index " +
						// index + ", is writing variable number: " +
						// astore.var);
						while(targetNode.getOpcode() != Opcodes.ISTORE)
						{
							if(targetNode instanceof MethodInsnNode && targetNode.getOpcode() != Opcodes.INVOKEINTERFACE)
							{
								MethodInsnNode mNode = (MethodInsnNode) targetNode;
								// System.out.printf("found deletion target at index %d: %s\n",
								// index, insnToString(mNode));
								found = true;
								iter.remove();
								targetNode = iter.next();
								break;
							}
							targetNode = iter.next();
							// System.out.print("Reading node: " +
							// insnToString(targetNode));
						}
						break;
					}
					index++;
				}
				if(found)
					m.instructions.insertBefore(targetNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/pengu/hammercore/api/dynlight/ProxiedDynlightGetter", "getLightValue", goalInvokeDesc, false));
				break;
			}
		}
		
//		if(add_func_72853_d)
//		{
//			HammerCoreCore.ASM_LOG.info("Sending instructions to World for function getMoonPhase");
//			
//			classNode.methods.add(getMoonPhase(obf ? "D" : "getMoonPhase"));
//			
//			HammerCoreCore.ASM_LOG.info("    Adding getMoonPhase (func_72853_d) back because we are on server.");
//		}
		
		return ObjectWebUtils.writeClassToByteArray(classNode);
	}
	
	private MethodNode getMoonPhase(String name)
	{
		MethodNode func_72853_d = new MethodNode(Opcodes.ASM5);
		func_72853_d.desc = "()I";
		func_72853_d.access = Opcodes.ACC_PUBLIC;
		func_72853_d.exceptions = new ArrayList<>();
		func_72853_d.name = name;
		InsnList list = new InsnList();
		list.add(new VarInsnNode(Opcodes.ALOAD, 0));
		list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/pengu/hammercore/asm/WorldHooks", "getMoonPhase", "(L" + classNameWorld + ";)I"));
		list.add(new InsnNode(Opcodes.IRETURN));
		func_72853_d.instructions = list;
		return func_72853_d;
	}
}