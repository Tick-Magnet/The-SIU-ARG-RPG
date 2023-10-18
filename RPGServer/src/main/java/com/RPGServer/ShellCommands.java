package com.RPGServer;

import com.RPGServer.EncounterSystem.Encounter;
import com.RPGServer.ItemSystem.*;
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
		Encounter encounter = null;
		if(encounterDefPath.equals("e"))
			encounter = new Encounter(user, "/EncounterDefinitions/ratEncounter.JSON");
		else
			encounter = new Encounter(user, encounterDefPath);
		RpgServerApplication.encounterMap.put(encounter.uuid, encounter);

		return encounter.uuid;

	}

	@ShellMethod(key = "healPlayer")
	public void healPlayer(String username)
	{
		UserAccount user = userAccountRepository.findByUsername(username);
		user.playerCharacter.resetHealth();
	}

	@ShellMethod(key = "giveItem")
	public void giveItem(String username, String itemDefinition) throws IOException
	{
		ItemFactory itemFactory = new ItemFactory();
		UserAccount user = userAccountRepository.findByUsername(username);
		user.playerCharacter.addItem(itemFactory.getItem(itemDefinition));
		userAccountRepository.save(user);
	}
	
	@ShellMethod(key = "createTestAccount")
	public void createTestAccount(String username)
	{
		UserAccount user = new UserAccount();
		user.setUsername("spawnedUser");
		
		user.playerCharacter = new PlayerCharacter();
	}
}
