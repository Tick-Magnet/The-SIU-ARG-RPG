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
	
	@ShellMethod(key = "setUserRole)
	public String setUserRole(String username, String userRole)
	{
		
	}
}
