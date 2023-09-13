package com.RPGServer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class UserAccount
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer Id;
	
	private String username; 
	
	@Column(columnDefinition="BLOB(32)")
	private byte passwordHash[];
	
	private byte[] sessionToken;
	private String tokenExpiration;
	
	public UserAccount()
	{
		this.username = null;
		this.passwordHash = null;
	}
	
	public void setPassword(String newPassword) throws NoSuchAlgorithmException
	{
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(newPassword.getBytes(StandardCharsets.UTF_8));
		byte newPasswordHash[] = messageDigest.digest();
		
		passwordHash = newPasswordHash;
	}
	
	public void setUsername(String newUsername)
	{
		this.username = newUsername;
	}
	
	public byte[] getPasswordHash()
	{
		return passwordHash;
	}
	
	public void setSessionToken(byte[] newToken)
	{
		this.sessionToken = newToken;
	}
	
	public byte[] getSessionToken()
	{
		return sessionToken;
	}
	
	public void setTokenExpiration(String expirationDate)
	{
		this.tokenExpiration = expirationDate;
	}
	
	public String getTokenExpiration()
	{
		return tokenExpiration;
	}
}
