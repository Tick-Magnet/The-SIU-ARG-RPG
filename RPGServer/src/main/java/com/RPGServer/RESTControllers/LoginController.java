package com.RPGServer.RESTControllers;


import com.RPGServer.Security.RateLimitService;
import com.RPGServer.SessionToken;
import com.RPGServer.UserAccount;
import com.RPGServer.UserAccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import java.util.Base64;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.*;

import java.util.Arrays;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;


@RestController
public class LoginController
{
	//Number of hours a token should be valid for 
	public final int HOURS_TOKEN_VALID = 24;
	
	
	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private RateLimitService rateLimitService;
	
	@PostMapping("/login")
	public Map<String,Object> login(@RequestBody Map<String, Object> payload, HttpServletRequest request) throws NoSuchAlgorithmException
	{
		System.out.println(request.getRemoteAddr());
		HashMap<String, Object> output = new HashMap<String, Object>();
		//Check if client is blocked from rate limit service
		if(rateLimitService.filterIP(request.getRemoteAddr(), RateLimitService.CallType.LOGIN) == false)
		{
			System.out.println(request.getRemoteAddr());
			return null;
		}
		String username = (String)payload.get("username");
		String password = (String)payload.get("password");
		UserAccount tempAccount = userAccountRepository.findByUsername(username);
		password = password + tempAccount.getPasswordSalt();
		
		SessionToken token = null;
		SecureRandom tokenGen = new SecureRandom();
		Base64.Encoder encoder = Base64.getUrlEncoder();
		//Validate user-----------------------
		//Hash password from HTTP request using SHA-256
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(password.getBytes(StandardCharsets.UTF_8));
		byte passwordHash[] = messageDigest.digest();
		//Retrieve users password hash from database and compare

		//If valid, generate session token------------------
		if(tempAccount != null && tempAccount.getVerified() && Arrays.equals(passwordHash, tempAccount.getPasswordHash()))
		{
			System.out.println("Old token: " + tempAccount.getSessionToken());
			System.out.println("Sucessful login for" + username);
			//Generate a 32 byte token for future API access for user
			byte[] tokenArray = new byte[32];
			tokenGen.nextBytes(tokenArray);
			token = new SessionToken(encoder.encodeToString(tokenArray), LocalDateTime.now(ZoneId.of("UTC")).plusHours(HOURS_TOKEN_VALID).toString());
			output.put("token", encoder.encodeToString(tokenArray));
			output.put("expirationDate", token.expirationDate());
			output.put("username", username);
			output.put("valid", true);
			output.put("userRole", tempAccount.userRole.toString());
			output.put("characterCreated", tempAccount.playerCharacter.creationComplete);
			//Store token to user account and update in database
			tempAccount.setSessionToken(tokenArray);
			tempAccount.setTokenExpiration(token.getExpirationDate());
			userAccountRepository.save(tempAccount);
		}
		else if(tempAccount != null && tempAccount.getVerified())
		{
			//Update bad passwords for this client
			rateLimitService.badLogin(request.getRemoteAddr());
			output.put("valid", false);
			output.put("message", "Bad password");
		}
		else if(tempAccount != null && tempAccount.getVerified() == false)
		{
			output.put("message", "Please verify your account first, check your email");
		}
		else
		{
			output.put("message", "Account does not exist");
		}

		//Return response
		return output;
	}
	
	//Simple request to verify session token is still valid
	@PostMapping("/checktoken")
	public Map<String,Object> checkToken(@RequestBody Map<String, Object> payload)
	{
		String username = (String)payload.get("username");
		String token = (String)payload.get("token");
		System.out.println("checking token: " + token);
		HashMap<String, Object> output = new HashMap<String,Object>();
		System.out.println(token);
		UserAccount tempAccount = userAccountRepository.findByUsername(username);
		if(tempAccount != null)
		{
			if(tempAccount.isValidSessionToken(token) == true)
			{
				output.put("valid", true);
				output.put("userRole", tempAccount.userRole.toString());
				output.put("characterCreated", tempAccount.playerCharacter.creationComplete);
			}
            else
            {
                output.put("valid", false);
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
