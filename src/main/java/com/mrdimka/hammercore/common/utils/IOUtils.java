package com.mrdimka.hammercore.common.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.function.Supplier;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.imageio.ImageIO;

public class IOUtils
{
	public static int heapLimit = 4096;
	public static final byte[] ZERO_ARRAY = new byte[0];
	
	private static final ThreadLocal<byte[]> buf = ThreadLocal.withInitial(new Supplier<byte[]>()
	{
		@Override
		public byte[] get()
		{
			return new byte[heapLimit];
		}
	});
	
	public static BufferedImage downloadPicture(String url)
	{
		try
		{
			return ImageIO.read(new URL(url));
		}
		catch(Throwable err) {}
		
		return null;
	}
	
	public static byte[] downloadData(String url)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			downloadAndWriteData(url, baos);
			byte[] buf = baos.toByteArray();
			return buf;
		}
		catch(Throwable err) {}
		
		return ZERO_ARRAY;
	}
	
	public static void downloadAndWriteData(String url, OutputStream o)
	{
		try
		{
			URL u = new URL(url);
			InputStream input = u.openConnection().getInputStream();
			pipeData(input, o);
			input.close();
		}
		catch(Throwable err) {}
	}
	
	public static void pipeData(InputStream from, OutputStream to)
	{
		try
		{
			byte[] buf = IOUtils.buf.get();
			int read = 0;
			while((read = from.read(buf)) > 0) to.write(buf, 0, read);
		}
		catch(Throwable err) {}
	}
	
	public static byte[] pipeOut(InputStream from)
	{
		ByteArrayOutputStream to = new ByteArrayOutputStream();
		pipeData(from, to);
		return to.toByteArray();
	}
	
	public static byte[] pipeOutAvaliable(InputStream from)
	{
		try
		{
			byte[] buf = new byte[from.available()];
			from.read(buf);
			return buf;
		}
		catch(Throwable err) {}
		
		return ZERO_ARRAY;
	}
	
	public static byte[] deflaterCompress(byte[] data)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DeflaterOutputStream o = new DeflaterOutputStream(baos, new Deflater(Deflater.BEST_COMPRESSION));
			o.write(data);
			o.close();
			return baos.toByteArray();
		}
		catch(Throwable err) {}
		return ZERO_ARRAY;
	}
	
	public static byte[] deflaterUncompress(byte[] data)
	{
		try
		{
			InflaterInputStream i = new InflaterInputStream(new ByteArrayInputStream(data), new Inflater());
			data = pipeOut(i);
			i.close();
			return data;
		}
		catch(Throwable err) {}
		return ZERO_ARRAY;
	}
}