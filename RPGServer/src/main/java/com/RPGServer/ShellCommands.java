package com.RPGServer;

import org.springframework.shell.standard.*;
import org.springframework.beans.factory.annotation.Autowired;

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
}
