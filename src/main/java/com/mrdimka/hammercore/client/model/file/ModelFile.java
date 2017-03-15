package com.mrdimka.hammercore.client.model.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Set;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import com.mrdimka.hammercore.common.utils.IOUtils;

public class ModelFile implements Serializable
{
	public int textureWidth, textureHeight;
	public Set<ModelPart> parts;
	
	public byte[] write()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		write(baos);
		return baos.toByteArray();
	}
	
	public void write(OutputStream stream)
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(this);
			out.close();
			stream.write(IOUtils.deflaterCompress(baos.toByteArray()));
		}
		catch(Throwable err) {}
	}
	
	public static ModelFile read(File file)
	{
		try
		{
			FileInputStream i = new FileInputStream(file);
			ModelFile f = read(i);
			i.close();
			return f;
		}
		catch(Throwable err) {}
		return null;
	}
	
	public static ModelFile read(URL url)
	{
		try { return read(url.openStream()); } catch(Throwable err) {}
		return null;
	}
	
	public static ModelFile read(InputStream data)
	{
		try
		{
			data = new ByteArrayInputStream(IOUtils.deflaterUncompress(IOUtils.pipeOut(data)));
			ObjectInputStream ois = new ObjectInputStream(data);
			ModelFile f = (ModelFile) ois.readObject();
			ois.close();
			return f;
		}
		catch(Throwable err) {}
		return null;
	}
	
	public static ModelFile read(byte[] data)
	{
		return read(new ByteArrayInputStream(data));
	}
}