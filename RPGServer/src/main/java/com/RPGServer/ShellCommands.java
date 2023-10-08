package com.RPGServer;

import com.RPGServer.EncounterSystem.Encounter;
import org.apache.catalina.User;
import org.springframework.shell.standard.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.UUID;

@ShellComponent
public class ShellCommands
{
	
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@ShellMethod(key = "test")
	public String test(@ShellOption(defaultValue = "test") String input)
	{
		return input;
	}
	
	@ShellMethod(key = "setUserRole")
	public String setUserRole(String username, String userRole)
	{
		String output = "User " + username + " does not exist";
		UserAccount targetUser = userAccountRepository.findByUsername(username);
		if(targetUser != null)
		{
			//Set user account role to passed role
			UserAccount.UserRole role = UserAccount.UserRole.valueOf(userRole);
			targetUser.userRole = role;
			//Save user to repository
			userAccountRepository.save(targetUser);
			output = username + " user role set to " + role.toString();
		}
		return output;
	}

	@ShellMethod(key = "createEncounter")
	public UUID createEncounter(String username, String encounterDefPath) throws IOException
	{
		UserAccount user  = userAccountRepository.findByUsername(username);
		Encounter encounter = new Encounter(user, "/EncounterDefinitions/ratEncounter.JSON");
		RpgServerApplication.encounterMap.put(encounter.uuid, encounter);

		return encounter.uuid;

	}
}
