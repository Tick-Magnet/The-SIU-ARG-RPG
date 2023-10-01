package com.RPGServer;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.math.BigInteger;
import java.util.Base64;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.*;

import java.util.Arrays;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;


@RestController
public class LoginController
{
	//Number of hours a token should be valid for 
	public final int HOURS_TOKEN_VALID = 24;
	
	
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@PostMapping("/login")
	public SessionToken login(@RequestBody Map<String, Object> payload) throws NoSuchAlgorithmException
	{
		String username = (String)payload.get("username");
		String password = (String)payload.get("password");
		
		SessionToken token = null;
		SecureRandom tokenGen = new SecureRandom();
		Base64.Encoder encoder = Base64.getUrlEncoder();
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
			token = new SessionToken(encoder.encodeToString(tokenArray), LocalDateTime.now(ZoneId.of("UTC")).plusHours(HOURS_TOKEN_VALID).toString());
			//Store token to user account and update in database
			tempAccount.setSessionToken(tokenArray);
			tempAccount.setTokenExpiration(token.getExpirationDate());
			userAccountRepository.save(tempAccount);
		}
		//Return response
		return token;
	}
	
	//Simple request to verify session token is still valid
	@PostMapping("/checktoken")
	public boolean checkToken(@RequestBody Map<String, Object> payload)
	{
		String username = (String)payload.get("username");
		String token = (String)payload.get("token");
		
		boolean output = false;
		System.out.println(token);
		UserAccount tempAccount = userAccountRepository.findByUsername(username);
		if(tempAccount != null)
		{
			if(tempAccount.isValidSessionToken(token) == true)
			{
				output = true;
			}
		}
		
		return output;
	}
	
	//test request
	@GetMapping("/test")
	public String test()
	{
		return "Test String";
	}
}
