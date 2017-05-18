package com.mrdimka.hammercore.common.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5
{
	public static String encrypt(byte[] data)
	{
		MessageDigest messageDigest = null;
		byte[] digest = new byte[0];
		
		try
		{
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(data);
			digest = messageDigest.digest();
		} catch(NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		
		BigInteger bigInt = new BigInteger(1, digest);
		String md5Hex = bigInt.toString(16);
		while(md5Hex.length() < 32)
			md5Hex = "0" + md5Hex;
		return md5Hex;
	}
	
	public static String encrypt(String line)
	{
		return encrypt(line.getBytes());
	}
}