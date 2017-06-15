package com.pengu.hammercore.asm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.google.common.base.Throwables;

/**
 * Simple implementation of CSV file reader
 */
public class CSVFile
{
	public final Map<String, String> OBFUSCATIONS = new HashMap<>();
	public final Map<String, String> OBFUSCATIONS_REVERSED = new HashMap<>();
	
	public CSVFile(File file)
	{
		read(file);
	}
	
	public CSVFile(InputStream in)
	{
		read(in);
	}
	
	public String getObfuscatedName(String deobf)
	{
		return OBFUSCATIONS_REVERSED.containsKey(deobf) ? OBFUSCATIONS_REVERSED.get(deobf) : deobf;
	}
	
	public String getDeobfuscatedName(String obf)
	{
		return OBFUSCATIONS.containsKey(obf) ? OBFUSCATIONS.get(obf) : obf;
	}
	
	public Set<String> getDeobfuscateds()
	{
		return OBFUSCATIONS_REVERSED.keySet();
	}
	
	public Set<String> getObfuscateds()
	{
		return OBFUSCATIONS.keySet();
	}
	
	public void read(File file)
	{
		try
		{
			read(new FileInputStream(file));
		} catch(FileNotFoundException e)
		{
			Throwables.propagate(e);
		}
	}
	
	public void read(InputStream in)
	{
		OBFUSCATIONS_REVERSED.clear();
		OBFUSCATIONS.clear();
		
		Scanner s = new Scanner(in);
		
		s.nextLine();
		
		while(s.hasNextLine())
		{
			String obf = s.nextLine();
			if(obf.contains(","))
			{
				String[] elems = obf.split(",");
				OBFUSCATIONS_REVERSED.put(elems[1], elems[0]);
				OBFUSCATIONS.put(elems[0], elems[1]);
			}
		}
		
		s.close();
	}
}