package com.RPGServer.RESTControllers;


import com.RPGServer.UserAccount;
import com.RPGServer.UserAccountRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;

import java.util.UUID;

import org.springframework.web.servlet.ModelAndView;


@RestController
public class AccountVerifyController
{
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@GetMapping("/verify/{username}/{token}")
	public ModelAndView verify(@PathVariable("username") String username, @PathVariable("token") String token)
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
		//Redirect to verification page
		ModelAndView view = new ModelAndView("verify.html");
		return view;
	}
}
