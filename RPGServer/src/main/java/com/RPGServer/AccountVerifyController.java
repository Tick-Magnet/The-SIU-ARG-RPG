package com.RPGServer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;

import java.util.UUID;


@RestController
public class AccountVerifyController
{
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@GetMapping("/verify/{username}/{token}")
	public String verify(@PathVariable("username") String username, @PathVariable("token") String token)
	{
		String response = "Unable to verify account";
		//retrieve account
		UserAccount account = userAccountRepository.findByUsername(username);
		if(account.getVerifyToken().equals(UUID.fromString(token)))
		{
			account.setVerified(true);
			userAccountRepository.save(account);
			response = "Account verified";
		}
		
		return response;
	}
}
