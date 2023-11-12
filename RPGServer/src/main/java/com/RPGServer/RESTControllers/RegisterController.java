package com.RPGServer.RESTControllers;

import java.util.concurrent.atomic.AtomicLong;

import com.RPGServer.UserAccount;
import com.RPGServer.UserAccountRepository;
import com.RPGServer.PlayerCharacter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
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

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.UUID;
import java.util.Map;

@RestController
public class RegisterController
{
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@Autowired
	private JavaMailSender mailSender;
	
	final private int MIN_PASSWORD_LENGTH = 8;
	final private String MAIL_USER_NAME = "CS435RPGProject";
	
	@PostMapping("/register")
	public RegistrationResult register(@RequestBody Map<String, Object> payload) throws NoSuchAlgorithmException
	{
		String username = (String)payload.get("username");
		String email = (String)payload.get("email");
		String password = (String)payload.get("password");
		
		RegistrationResult result = null;
		SecureRandom tokenGen = new SecureRandom();
		//create new UserAccount object
		UserAccount tempUser = new UserAccount();
		//Check if username is taken
		boolean usernameAvailable = false;
		if(userAccountRepository.findByUsername(username) == null && username != null)
			usernameAvailable = true;
		//check if password is valid
		if(isValidPassword(password) && usernameAvailable)
		{
			//Set username
			tempUser.setUsername(username);
			//Saving early to prevent a double send from user
			//Waiting for email to send takes too long, front end should be written to prevent double sending this call as well
			userAccountRepository.save(tempUser);
			//Set user password
			tempUser.setPassword(password);
			//Set user email
			tempUser.setEmail(email);
			tempUser.userRole = UserAccount.UserRole.USER;
			//Generate verification token
			UUID token = UUID.randomUUID();
			tempUser.setVerifyToken(token);
			tempUser.playerCharacter = new PlayerCharacter();
			//Send verification email
			try {
				SimpleMailMessage verifyMessage = new SimpleMailMessage();
				verifyMessage.setFrom(MAIL_USER_NAME);
				verifyMessage.setTo(email);
				verifyMessage.setText("Click on the link to activate your account\n" + "http://localhost:8080/verify/" + username + "/" + token.toString());
				verifyMessage.setSubject("CS435 RPG Email Verification");
				mailSender.send(verifyMessage);

				//Write new user account to database
				//NEED TO DO THIS BEFORE SENDING EMAIL. EMAIL BEING SENT EVEN ON JAKARATA EXCEPTIONS
				userAccountRepository.save(tempUser);
				//update result
				result = new RegistrationResult(true, "Account created with username: " + username);
			}
			catch(Exception e)
			{
				result = new RegistrationResult(false, "Unable to create account, verify your email or try again later");
			}
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

