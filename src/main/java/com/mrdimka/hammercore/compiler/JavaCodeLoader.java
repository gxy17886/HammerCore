package com.mrdimka.hammercore.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mrdimka.hammercore.HammerCore;
import com.mrdimka.hammercore.common.utils.IOUtils;

public class JavaCodeLoader
{
	public static Map<String, byte[]> compileRoot(File dir) throws IOException
	{
		Map<String, byte[]> classes = new HashMap<>();
		addClass(dir, classes);
		return classes;
	}
	
	private static void addClass(File java, Map<String, byte[]> classses) throws IOException
	{
		if(java.isDirectory())
		{
			for(File f : java.listFiles())
				addClass(f, classses);
			return;
		} else
		{
			String $package = java.getName();
			if(!$package.endsWith(".jc"))
				return;
			$package = $package.substring(0, $package.length() - 3);
			FileInputStream fis = new FileInputStream(java);
			String src = new String(IOUtils.pipeOut(fis)).replaceAll("\r\n", "\n");
			String[] lines = src.split("\n");
			
			if(HammerCore.IS_OBFUSCATED_MC)
				for(int i = 0; i < lines.length; ++i) // Obfuscate sources
				{
					String ln = lines[i];
					if(ln.startsWith("import"))
						continue;
					
					for(String deobf : HammerCore.FIELD_CSV.getDeobfuscateds())
					{
						String obf = HammerCore.FIELD_CSV.getObfuscatedName(deobf);
						while(ln.contains(deobf) && ln.charAt(ln.indexOf(deobf) - 1) == '.' && !((ln.charAt(ln.indexOf(deobf) + deobf.length()) >= 'a' && ln.charAt(ln.indexOf(deobf) + deobf.length()) <= 'z') || (ln.charAt(ln.indexOf(deobf) + deobf.length()) >= 'A' && ln.charAt(ln.indexOf(deobf) + deobf.length()) <= 'Z')))
							ln = ln.replace(deobf, obf);
					}
					
					for(String deobf : HammerCore.METHODS_CSV.getDeobfuscateds())
					{
						String obf = HammerCore.METHODS_CSV.getObfuscatedName(deobf);
						while(ln.contains(deobf) && ln.charAt(ln.indexOf(deobf) - 1) == '.' && !((ln.charAt(ln.indexOf(deobf) + deobf.length()) >= 'a' && ln.charAt(ln.indexOf(deobf) + deobf.length()) <= 'z') || (ln.charAt(ln.indexOf(deobf) + deobf.length()) >= 'A' && ln.charAt(ln.indexOf(deobf) + deobf.length()) <= 'Z')))
							ln = ln.replace(deobf, obf);
					}
					
					lines[i] = ln;
				}
			
			String compiled = bakeClass(lines, $package);
			
			File jco = new File("javacode_out");
			if(!jco.isDirectory())
				jco.mkdirs();
			
			FileOutputStream out = new FileOutputStream(new File("javacode_out", $package + ".java"));
			out.write(compiled.getBytes());
			out.close();
			
			JavaCompilerImpl.compileFileIntoFolder("\"" + new File("javacode_out", $package + ".java").getAbsolutePath() + "\"", ".");
			
			FileInputStream $fis = new FileInputStream($package + ".class");
			byte[] claz = IOUtils.pipeOut($fis);
			$fis.close();
			
			new File($package + ".class").delete();
			
			classses.put($package, claz);
			fis.close();
		}
	}
	
	public static String bakeClass(String[] lines, String clsName)
	{
		String cls = "public class " + clsName + " \n{";
		String mth = null;
		Set<String> imports = new HashSet<>();
		
		for(String ln : lines)
		{
			if(ln.isEmpty())
				continue;
			if(ln.startsWith("import ") && ln.endsWith(";"))
			{
				imports.add(ln);
				if(mth != null)
					throw new RuntimeException("Imports inside of methods are not allowed!");
			} else if(ln.startsWith("@"))
			{
				if(mth != null)
				{
					cls += mth + "	}";
					mth = null;
				}
				
				String[] subln = ln.substring(1).split(" ");
				mth = "\n	public static " + subln[0] + " " + subln[1] + "(";
				for(int i = 2; i < subln.length; ++i)
					mth += subln[i] + " ";
				mth += ") \n	{\n";
			} else if(mth != null)
				mth += "		" + ln + "\n";
		}
		
		if(mth != null)
		{
			cls += mth + "	}";
			mth = null;
		}
		
		String src = "";
		
		for(String i : imports)
			src += i + "\n";
		src += "\n";
		
		return src + cls + "\n}";
	}
	
	public static ClassLoader toLoader(Map<String, byte[]> classes)
	{
		return new ClassLoader()
		{
			@Override
			protected Class<?> findClass(String name) throws ClassNotFoundException
			{
				if(classes.containsKey(name))
					return defineClass(name, classes.get(name), 0, classes.get(name).length);
				return super.findClass(name);
			}
		};
	}
}