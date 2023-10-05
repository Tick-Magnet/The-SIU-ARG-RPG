package com.RPGServer.RESTControllers;


import com.RPGServer.EncounterSystem.Encounter;
import com.RPGServer.UserAccountRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;


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
