package com.mrdimka.hammercore.compiler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

public class JavaCompilerImpl
{
	public static byte[] compile(byte[] source, String srcName)
	{
		// Define package? Sure!
		String srcPackage = "";
		String srcStr = new String(source).trim();
		if(srcStr.startsWith("package "))
			srcPackage = srcStr.substring(8, srcStr.indexOf(";"));
		char[] srcPackageChars = srcPackage.toCharArray();
		for(int i = 0; i < srcPackageChars.length; ++i)
			if(srcPackageChars[i] == '.')
				srcPackageChars[i] = File.separatorChar;
		String packagePath = new String(srcPackageChars);
		
		File inf = new File(System.getProperty("java.io.tmpdir"), "jdt_in" + System.currentTimeMillis());
		final File infroot = inf;
		infroot.deleteOnExit();
		if(!packagePath.isEmpty())
			inf = new File(inf, packagePath);
		File outf = new File(System.getProperty("java.io.tmpdir"), "jdt_out" + System.currentTimeMillis());
		outf.deleteOnExit();
		
		byte[] comp = new byte[0];
		
		// while(srcName.contains(".")) srcName =
		// srcName.substring(srcName.indexOf("."));
		
		try
		{
			inf.mkdirs();
			File src = new File(inf, srcName + ".java");
			FileOutputStream fos = new FileOutputStream(src);
			fos.write(source);
			fos.close();
			
			compileFileIntoFolder("\"" + inf.getAbsolutePath() + "\"", "\"" + outf.getAbsolutePath() + "\"");
			
			src.delete();
			
			File out;
			
			if(!packagePath.isEmpty())
				out = new File(new File(outf, packagePath), srcName + ".class");
			else
				out = new File(outf, srcName + ".class");
			
			if(!out.isFile())
				out = new File(outf, packagePath).listFiles()[0];
			
			FileInputStream fis = new FileInputStream(out);
			
			ByteArrayOutputStream to = new ByteArrayOutputStream();
			try
			{
				byte[] buf = new byte[8192];
				int read = 0;
				while((read = fis.read(buf)) > 0)
					to.write(buf, 0, read);
			} catch(Throwable err)
			{
			}
			comp = to.toByteArray();
			
			fis.close();
		} catch(IOException e)
		{
			e.printStackTrace();
		}
		
		delete(infroot);
		delete(outf);
		
		return comp;
	}
	
	public static boolean compileFileIntoFolder(String src, String dest)
	{
		try
		{
			Class comp = Class.forName("com.mrdimka.compiler.JavaCompiler");
			Method mth = comp.getDeclaredMethod("compileFileIntoFolder", String.class, String.class);
			mth.setAccessible(true);
			mth.invoke(null, src, dest);
			return true;
		} catch(Throwable err)
		{
		}
		return false;
	}
	
	public static void compileFileIntoJar(String src, File jarFile) throws IOException
	{
		File tmp = new File(System.getProperty("java.io.tmpdir"), "jdt" + System.currentTimeMillis());
		
		try
		{
			compileFileIntoFolder(src, "\"" + tmp.getAbsolutePath() + "\"");
			JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile), new Manifest());
			
			if(tmp.listFiles() != null)
				for(File f : tmp.listFiles())
				{
					jos.putNextEntry(new ZipEntry(f.getName()));
					FileInputStream from = new FileInputStream(f);
					
					byte[] buf = new byte[4096];
					int read = 0;
					while((read = from.read(buf)) > 0)
						jos.write(buf, 0, read);
					
					from.close();
					jos.closeEntry();
				}
			
			jos.close();
		} catch(Throwable err)
		{
			throw err;
		} finally
		{
			delete(tmp);
		}
	}
	
	private static void delete(File file)
	{
		if(file.isFile())
			file.delete();
		if(file.isDirectory())
			for(File child : file.listFiles())
				delete(child);
	}
}