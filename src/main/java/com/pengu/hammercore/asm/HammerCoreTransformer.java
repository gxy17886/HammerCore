package com.pengu.hammercore.asm;

import java.util.ListIterator;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Transforms classes
 */
public class HammerCoreTransformer implements IClassTransformer
{
	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass)
	{
		return handleTransform(name, transformedName, basicClass);
	}
	
	private byte[] handleTransform(String name, String transformedName, byte[] basicClass)
	{
		{
			String CL_OBF = "rb", CL_DEOBF = "net.minecraft.util.HttpUtil", FN_OBF = "a", FN_DEOBF = "getSuitableLanPort";
			
			if(name.equals(CL_OBF) || name.equals(CL_DEOBF) || transformedName.equals(CL_DEOBF))
			{
				boolean obf = name.equals(CL_OBF);
				HammerCoreCore.ASM_LOG.info("Transforming net.minecraft.util.HttpUtil (" + name + ")...");
				
				ClassNode classNode = ObjectWebUtils.loadClass(basicClass);
				
				String targetMethod;
				if(obf)
					targetMethod = FN_OBF;
				else
					targetMethod = FN_DEOBF;
				
				for(MethodNode m : classNode.methods)
					if(m.name.equals(targetMethod) && m.desc.equals("()I"))
					{
						int index = -1;
						AbstractInsnNode instruction = null;
						
						ListIterator<AbstractInsnNode> instructions = m.instructions.iterator();
						while(instructions.hasNext())
						{
							index++;
							instruction = instructions.next();
							if(instruction.getOpcode() == 3)
							{
								AbstractInsnNode toRemove = m.instructions.get(index);
								m.instructions.remove(toRemove);
								
								InsnList toInject = new InsnList();
								toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/pengu/hammercore/net/LanUtil", "getSuitableLanPort", "()I"));
								m.instructions.insertBefore(m.instructions.get(index), toInject);
								HammerCoreCore.ASM_LOG.info("-Sending instructions to HttpUtil for function getSuitableLanPort");
							}
						}
					}
				
				return ObjectWebUtils.writeClassToByteArray(classNode);
			}
		}
		
		return basicClass;
	}
}