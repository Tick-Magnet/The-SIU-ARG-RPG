package com.RPGServer;

import com.RPGServer.EncounterSystem.Encounter;
import com.RPGServer.ItemSystem.*;
import org.apache.catalina.User;
import org.springframework.shell.standard.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Instant;
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

	@ShellMethod(key = "ban")
	public String ban(String username, String banReason, int minutes)
	{
		String output = "User " + username + " does not exist";
		UserAccount targetUser = userAccountRepository.findByUsername(username);
		if(targetUser != null)
		{
			targetUser.unBanTime = Instant.now().plusSeconds(minutes*60);
			targetUser.banReason = banReason;
			//Save user to repository
			userAccountRepository.save(targetUser);
			System.out.println(Instant.now().toString());
			output = username + " banned for " + banReason +" until " + targetUser.unBanTime.toString();
		}
		return output;
	}
	@ShellMethod(key = "unBan")
	public String unBan(String username)
	{
		String output = "User " + username + " does not exist";
		UserAccount targetUser = userAccountRepository.findByUsername(username);
		if(targetUser != null)
		{
			targetUser.unBanTime = null;
			targetUser.banReason = null;
			output = username + " unbanned";
		}
		return output;
	}
	@ShellMethod(key = "showCommands")
	public String showCommands()
	{
		String output = "";
		Method[] methods = this.getClass().getMethods();
		System.out.println(methods.length);
		for(int i = 0; i < methods.length; i++)
		{

			output = output.concat(methods[i].getName() + "\n");
		}

		return output;
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
