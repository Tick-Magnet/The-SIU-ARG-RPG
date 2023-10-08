package com.RPGServer.EncounterSystem;
import com.RPGServer.ItemSystem.*;

import com.RPGServer.UserAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;

import java.io.InputStreamReader;
import java.util.UUID;
import java.util.ArrayList;


//Encounter system written in a way so that JSON files can be used to create new game content
public class Encounter
{
	public String encounterName;
	
	//Reference to the player who initiated the encounter
	private UserAccount playerAccount;
	
	//Contains all steps of encounter, used so generated steps can refer to steps by name
	public EncounterStep[] encounterSteps;
	
	public EncounterEntity[] entityArray;
	
	public EncounterStep currentStep;

	//Rewards to be granted to player on completion of encounter
	public Reward encounterReward;
	
	public UUID uuid;
	
	public void nextEncounterStep(int stepIndex)
	{
		//a negative index will signal end of encounter
		if(stepIndex < 0)
		{
			endEncounter();
		}
		else
		{
			currentStep = encounterSteps[stepIndex];
		}
	}
	
	//Actions to be completed when encounter is over
	private void endEncounter()
	{
		
	}
	
	//Construct encounter based on the JSON file passed
	public Encounter(UserAccount user, String encounterDefinitionPath) throws IOException
	{
		playerAccount = user;
		ObjectMapper objectMapper = new ObjectMapper();
		uuid = UUID.randomUUID();
		encounterReward = new Reward();

		//ClassLoader loader = Thread.currentThread().getContextClassLoader();
		//File jsonFile = new File(loader.getResource(encounterDefinitionPath).getPath());
		InputStream inputStream = getClass().getResourceAsStream(encounterDefinitionPath);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		//JsonNode rootNode = objectMapper.readTree((new ClassPathResource(encounterDefinitionPath)).getFile());
		JsonNode rootNode = objectMapper.readTree(inputStream);
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
					encounterSteps[i].parentEncounter = this;
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
					encounterSteps[i] = new CombatStep(tempNode.get("enemyIndex").asInt(), tempNode.get("nextStepIndex").asInt());
					encounterSteps[i].parentEncounter = this;
				break;
			}
				encounterSteps[i].parentEncounter = this;
		}
		currentStep = encounterSteps[0];
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
			entityArray[i].health = entityArray[i].constitution;
		}
	}
	
	public StepUpdate postStepUpdate(StepUpdate update)
	{
		if(update.selectedChoice == -1)
		{
			return currentStep.getInitialStepUpdate();
		}
		else
		{
			return currentStep.postStepUpdate(update);
		}
	}
	
	public void updateCurrentStep(EncounterStep nextStep)
	{
		this.currentStep = nextStep;
	}
	
	public String listEntities()
	{
		String output = "";
		for(int i = 0; i < entityArray.length; i++)
		{
			output = output + entityArray[i].name + "\n";
			output = output + entityArray[i].imagePath + "\n";
			output = output + entityArray[i].strength + "\n";
			output = output + entityArray[i].dexterity + "\n";
			output = output + entityArray[i].constitution + "\n";
			output = output + entityArray[i].intelligence + "\n";
			output = output + "\n";
		}
		
		return output;
	}
	
	public UserAccount getUserAccount()
	{
		return this.playerAccount;
	}
	
	//Class to contain data about enemies, player, and other information that may need to be persistent between encounter steps
	public class EncounterEntity
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
	//store rewards added by encounter steps
	public class Reward
	{
		int experienceReward;
		int goldReward;
		ArrayList<Item> itemRewards;

		public Reward()
		{
			itemRewards = new ArrayList<Item>();
			experienceReward = 0;
			goldReward = 0;
		}

		public void grantReward(UserAccount player)
		{
			//Add experience
			player.playerCharacter.applyExperience(experienceReward);
			//Add gold reward
			player.playerCharacter.gold += goldReward;

			//Add items to player inventory
		}
	}
}
