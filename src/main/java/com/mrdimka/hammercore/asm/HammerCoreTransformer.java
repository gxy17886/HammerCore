package com.mrdimka.hammercore.asm;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
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
	private String classNameWorld = "ajq";
	
	/*
	 * (Lnet/minecraft/util/BlockPos;Lnet/minecraft/world/EnumSkyBlock;)I / func_175638_a
	 */
	private String targetMethodDesc = "(Lco;Lajw;)I";
	
	/* net/minecraft/world/World.getRawLight / func_175638_a */
	private String computeLightValueMethodName = "a";
	
	/*
	 * (Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/BlockPos;)I
	 */
	private String goalInvokeDesc = "(Latj;Laju;Lco;)I";
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		if(name.equals(classNameWorld)) return handleWorldTransform(basicClass, true);
		else if(name.equals("net.minecraft.world.World"))
		{
			computeLightValueMethodName = "getRawLight";
			goalInvokeDesc = "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)I";
			return handleWorldTransform(basicClass, false);
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
//		System.out.println("**************** Dynamic Lights transform running on World, obf: " + obf + " *********************** ");
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		for(MethodNode m : classNode.methods)
		{
			if(m.name.equals(computeLightValueMethodName) && (!obf || m.desc.equals(targetMethodDesc)))
			{
//				System.out.println("In target method " + computeLightValueMethodName + ":" + m.desc + ", Patching!");
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
//						System.out.println("Found ASTORE Node at index " + index + ", is writing variable number: " + astore.var);
						while(targetNode.getOpcode() != Opcodes.ISTORE)
						{
							if(targetNode instanceof MethodInsnNode && targetNode.getOpcode() != Opcodes.INVOKEINTERFACE)
							{
								MethodInsnNode mNode = (MethodInsnNode) targetNode;
//								System.out.printf("found deletion target at index %d: %s\n", index, insnToString(mNode));
								found = true;
								iter.remove();
								targetNode = iter.next();
								break;
							}
							targetNode = iter.next();
//							System.out.print("Reading node: " + insnToString(targetNode));
						}
						break;
					}
					index++;
				}
				if(found) m.instructions.insertBefore(targetNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mrdimka/hammercore/api/dynlight/ProxiedDynlightGetter", "getLightValue", goalInvokeDesc, false));
				break;
			}
		}
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		return writer.toByteArray();
	}
}