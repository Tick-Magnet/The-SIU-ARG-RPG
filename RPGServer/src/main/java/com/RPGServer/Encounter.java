package com.RPGServer;

import java.io.File;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.util.HashMap;

//Encounter system written in a way so that JSON files can be used to create new game content
public class Encounter
{
	public String encounterName;
	
	//Reference to the player who initiated the encounter
	private UserAccount playerAccount;
	
	//Contains all steps of encounter, used so generated steps can refer to steps by name
	private EncounterStep[] encounterSteps;
	
	public EncounterEntity[] entityArray;
	
	private EncounterStep currentStep;
	
	//Construct encounter based on the JSON file passed
	public Encounter(UserAccount user, String encounterDefinitionPath) throws IOException
	{
		playerAccount = user;
		ObjectMapper objectMapper = new ObjectMapper();
		entityMap = new HashMap<String, EncounterEntity>();
		
		JsonNode rootNode = objectMapper.readTree(new File(encounterDefinitionPath));
		//Fill fields in Encounter class from JSON tree
		encounterName = rootNode.get("encounterName").asText();
		//Read encounter steps array
		JsonNode encounterStepsNode = rootNode.get("encounterSteps");
		encounterSteps = new EncounterStep[encounterStepsNode.size()];
		JsonNode tempNode;
		for(int i = 0; i < encounterStepsNode.size(); i++)
		{
			//Fill step object in array based on node
			tempNode = encounterStepsNode.get(i);
			switch(tempNode.get("stepType").asInt())
			{
				//Dialogue step
				case 0:
					encounterSteps[i] = new DialogueStep();
					//Add dialogue options
					JsonNode optionArrayNode = tempNode.get("dialogueOptions");
					for(int j = 0; j < optionArrayNode.size(); j++)
					{
						//Get current dialogue option from the array
						JsonNode option = optionArrayNode.get(j);
						//Have to cast to DialogueStep, stored in array as generic encounter step
						DialogueStep currentStep = (DialogueStep) encounterSteps[i];
						currentStep.addDialogueOption(option.get("optionText").asText(), option.get("nextStepIndex").asInt());
					}
				break;
				//Combat step
				case 1:
				
				break;
			}
		}
		//Read encounter entities array
		JsonNode entityArrayNode = rootNode.get("encounterEntities");
		entityArray = new EncounterEntity[entityArrayNode.size()];
		for(int i = 0; i < entityArray.length; i++)
		{
			//Get current entity from array node
			tempNode = entityArrayNode.get(i);
			//Fill EncounterEntity object fields from JSON file
			entityArray[i] = new EncounterEntity();
			entityArray[i].name = tempNode.get("name").asText();
			entityArray[i].imagePath = tempNode.get("imagePath").asText();
			entityArray[i].strength = tempNode.get("strength").asInt();
			entityArray[i].dexterity = tempNode.get("dexterity").asInt();
			entityArray[i].constitution = tempNode.get("constitution").asInt();
			entityArray[i].intelligence = tempNode.get("intelligence").asInt();
		}
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
		public String imagePath;
		//Stats
		public int strength;
		public int dexterity;
		public int constitution;
		public int intelligence;
		
		public int health;
		
	}
}
