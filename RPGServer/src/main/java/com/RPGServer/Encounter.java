package com.RPGServer;

import java.io.File;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;

//Encounter system written in a way so that JSON files can be used to create new game content
public class Encounter
{
	//Reference to the player who initiated the encounter
	private UserAccount playerAccount;
	
	//Contains all steps of encounter, used so generated steps can refer to steps by name
	private HashMap<String, EncounterStep> encounterStepsMap;
	
	private EncounterStep currentStep;
	
	//Construct encounter based on the JSON file passed
	public Encounter(UserAccount user, String encounterDefinitionPath)
	{
		playerAccount = user;
		encounterStepsMap = new HashMap<String, EncounterStep>();
		//Fill fields in Encounter class from JSON tree
		
		//
		
		
	}
	
	public StepUpdate postStepUpdate(StepUpdate update)
	{
		return currentStep.postStepUpdate(update);
	}
	
	public void updateCurrentStep(EncounterStep nextStep)
	{
		this.currentStep = nextStep;
	}
	
	//Class to contain data about enemies, player, and other information that may need to be persistent between encounter steps
	private class EncounterEntity
	{
		public String name;
		//Stats
		public int strength;
		public int dexterity;
		public int constitution;
		public int intelligence;
		
		public int health;
		
	}
}
