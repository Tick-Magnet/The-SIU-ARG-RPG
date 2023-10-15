package com.RPGServer;

import java.math.BigInteger;

public record SessionToken(String token, String expirationDate) 
{
	public String getToken()
	{
		return token;
	}
	
	public String getExpirationDate()
	{
		return expirationDate;
	}
}
