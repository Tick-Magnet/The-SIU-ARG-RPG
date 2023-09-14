package com.RPGServer;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.math.BigInteger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.*;

import java.util.Arrays;


@RestController
public class LoginController
{
	//Number of hours a token should be valid for 
	public final int HOURS_TOKEN_VALID = 24;
	
	
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@GetMapping("/login")
	public SessionToken login(@RequestParam(value = "username", required = true) String username, @RequestParam(value = "password", required = true) String password) throws NoSuchAlgorithmException
	{
		SessionToken token = null;
		SecureRandom tokenGen = new SecureRandom();
		//Validate user-----------------------
		//Hash password from HTTP request using SHA-256
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(password.getBytes(StandardCharsets.UTF_8));
		byte passwordHash[] = messageDigest.digest();
		//Retrieve users password hash from database and compare
		UserAccount tempAccount = userAccountRepository.findByUsername(username);
		//If valid, generate session token------------------
		if(tempAccount.getVerified() && Arrays.equals(passwordHash, tempAccount.getPasswordHash()))
		{
			System.out.println("Old token: " + tempAccount.getSessionToken());
			System.out.println("Sucessful login for" + username);
			//Generate a 32 byte token for future API access for user
			byte[] tokenArray = new byte[32];
			tokenGen.nextBytes(tokenArray);
			token = new SessionToken(tokenArray, LocalDateTime.now(ZoneId.of("UTC")).plusHours(HOURS_TOKEN_VALID).toString());
			//Store token to user account and update in database
			tempAccount.setSessionToken(token.getToken());
			tempAccount.setTokenExpiration(token.getExpirationDate());
			userAccountRepository.save(tempAccount);
		}
		//Return response
		return token;
	}
	
}
