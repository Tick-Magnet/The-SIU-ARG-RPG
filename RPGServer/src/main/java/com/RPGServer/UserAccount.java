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
	
	private String email;
	
	private boolean verified;
	
	@Column(columnDefinition="BLOB(32)")
	private byte passwordHash[];
	
	private byte[] sessionToken;
	
	private byte[] verifyToken;
	
	private String tokenExpiration;
	
	public UserAccount()
	{
		this.username = null;
		this.passwordHash = null;
		this.email = null;
		this.verified = false;
	}
	
	public void setPassword(String newPassword) throws NoSuchAlgorithmException
	{
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(newPassword.getBytes(StandardCharsets.UTF_8));
		byte newPasswordHash[] = messageDigest.digest();
		
		passwordHash = newPasswordHash;
	}
	
	public void setVerifyToken(byte[] token)
	{
		this.verifyToken = token;
	}
	
	public byte[] getVerifyToken()
	{
		return this.verifyToken;
	}
	
	public void setVerified(boolean verified)
	{
		this.verified = verified;
	}
	
	public boolean getVerified()
	{
		return verified;
	}
	
	public void setUsername(String newUsername)
	{
		this.username = newUsername;
	}
	
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public String getEmail()
	{
		return this.email;
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
