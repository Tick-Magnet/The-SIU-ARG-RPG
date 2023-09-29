package com.RPGServer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Entity
public class UserAccount
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer Id;
		
	private UUID uuid;
	
	public int type;
	
	//Array containing awards for redeeming
	
	//Path to encounter definition file
	String encounterDefinitionPath;
}
