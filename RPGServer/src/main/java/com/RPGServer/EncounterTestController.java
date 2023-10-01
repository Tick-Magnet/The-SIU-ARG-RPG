package com.RPGServer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;

import java.util.UUID;

import org.springframework.web.servlet.ModelAndView;


@RestController
public class EncounterTestController
{
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@GetMapping("/testEncounter")
	public String test()
	{
		String outputString = " ";
		try
		{
			Encounter testEncounter = new Encounter(null, "EncounterDefinitions/ratEncounter.JSON");
			
			outputString = testEncounter.listEntities();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return outputString;
	}
	

}
