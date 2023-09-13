package com.RPGServer;

import java.math.BigInteger;

public record SessionToken(byte[] token, String expirationDate) 
{
	public byte[] getToken()
	{
		return token;
	}
	
	public String getExpirationDate()
	{
		return expirationDate;
	}
}
