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

@RestController
public class RegisterController
{
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	final private int MIN_PASSWORD_LENGTH = 8;
	
	@GetMapping("/register")
	public RegistrationResult register(@RequestParam(value = "username", required = true) String username, @RequestParam(value = "password", required = true) String password) throws NoSuchAlgorithmException
	{
		RegistrationResult result = null;
		//create new UserAccount object
		UserAccount tempUser = new UserAccount();
		//Check if username is taken
		boolean usernameAvailable = false;
		if(userAccountRepository.findByUsername(username) == null)
			usernameAvailable = true;
		//check if password is valid
		if(isValidPassword(password) && usernameAvailable)
		{
			//Set username
			tempUser.setUsername(username);
			//Set user password
			tempUser.setPassword(password);
			//Write new user account to database
			userAccountRepository.save(tempUser);
			//update result
			result = new RegistrationResult(true, "Account created with username: " + username);
		}
		else
		{
			//return bad result with reason
			if(!usernameAvailable)
				result = new RegistrationResult(false, "Username already taken");
			else if(!isValidPassword(password))
				result = new RegistrationResult(false, "Bad password");
			else
				result = new RegistrationResult(false, "Unknown error");
			
		}
		return result;
	}
	
	private boolean isValidPassword(String password)
	{
		boolean output = false;
		
		if(password.length() >= MIN_PASSWORD_LENGTH)
		{
			output = true;
		}
		
		return output;
	}
	
	public record RegistrationResult(boolean registered, String status) {}
}

