package com.mrdimka.hammercore.common.blocks.tesseract;

public enum TransferMode
{
	SEND_RECEIVE, RECEIVE, SEND, DISABLED;
	
	public static TransferMode fromByte(byte val)
	{
		return values()[val % values().length];
	}
	
	public byte asByte()
	{
		return (byte) ordinal();
	}
	
	public boolean receives()
	{
		return this == RECEIVE || this == SEND_RECEIVE;
	}
	
	public boolean sends()
	{
		return this == SEND || this == SEND_RECEIVE;
	}
}