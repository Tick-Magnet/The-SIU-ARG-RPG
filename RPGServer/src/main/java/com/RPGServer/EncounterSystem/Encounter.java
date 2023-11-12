package com.RPGServer.EncounterSystem;
import com.RPGServer.Dice;
import com.RPGServer.ItemSystem.*;

import com.RPGServer.RpgServerApplication;
import com.RPGServer.UserAccount;
import com.RPGServer.UserAccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStreamReader;
import java.util.UUID;
import java.util.ArrayList;
import java.time.*;


//Encounter system written in a way so that JSON files can be used to create new game content
public class Encounter
{
	@Autowired
	private UserAccountRepository userAccountRepository;
	private static final int EXPIRATION_MINUTES = 5;
	private static final int TIME_OUT_MINUTES = 15;

	public boolean playerUpdated;
	public String encounterName;
	
	//Reference to the player who initiated the encounter
	public UserAccount playerAccount;
	
	//Contains all steps of encounter, used so generated steps can refer to steps by name
	public EncounterStep[] encounterSteps;
	
	public EncounterEntity[] entityArray;
	
	public EncounterStep currentStep;
	
	public boolean encounterComplete;
	
	//Time after encounter object is eligible to be deleted from the encounter hashmap
	public LocalDateTime expiration;
	public LocalDateTime timeout;

	//Rewards to be granted to player on completion of encounter
	public ArrayList<Reward> encounterRewards;
	
	public UUID uuid;
	
	public void nextEncounterStep(int stepIndex)
	{
		//a negative index will signal end of encounter
		if(stepIndex < 0)
		{
			endEncounter();
			//Sets step to default reward step so client can request reward information using postEncounterUpdate
			currentStep = new RewardStep();
			currentStep.parentEncounter = this;
		}
		else
		{
			currentStep = encounterSteps[stepIndex];
		}
	}
	
	//Actions to be completed when encounter is over
	public void endEncounter()
	{
		for(int i = 0; i < encounterRewards.size(); i++)
		{
			encounterRewards.get(i).grantReward(playerAccount);
		}
		//Set completed flag
		encounterComplete = true;
		//Set expiration date for 3 minutes later (allow time for client to send step update requests to get reward information)
		expiration = LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(EXPIRATION_MINUTES);
	}

	//Construct encounter based on the JSON file passed
	public Encounter(UserAccount user, String encounterDefinitionPath) throws IOException
	{
		playerAccount = user;
		ObjectMapper objectMapper = new ObjectMapper();
		uuid = UUID.randomUUID();
		timeout = LocalDateTime.now(ZoneId.of("UTC")).plusMinutes(TIME_OUT_MINUTES);
		encounterRewards = new ArrayList<Reward>();
		ItemFactory itemFactory = new ItemFactory();
		playerUpdated = false;
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
		JsonNode rewardArray;
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
					try
					{
						encounterSteps[i].backgroundImagePath = tempNode.get("backgroundImagePath").asText();
					}
					catch(NullPointerException e)
					{
						encounterSteps[i].backgroundImagePath = "";
					}
					try
					{
						encounterSteps[i].promptText = tempNode.get("promptText").asText();
					}
					catch (NullPointerException e)
					{
						encounterSteps[i].promptText = "";
					}
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
					//Add rewards
					rewardArray = tempNode.get("rewards");
					for(int j = 0; rewardArray != null && j < rewardArray.size(); j++)
					{
						JsonNode reward = rewardArray.get(j);
						Encounter.Reward tempReward = new Encounter.Reward();
						int optionIndex = reward.get("optionIndex").asInt();
						try
						{
							tempReward.experienceReward = reward.get("experienceReward").asInt();
						}
						catch(Exception e)
						{
							tempReward.experienceReward = 0;
						}
						try
						{
							tempReward.goldReward = reward.get("goldReward").asInt();
						}
						catch(Exception e)
						{
							tempReward.goldReward = 0;
						}

						JsonNode itemArray = reward.get("itemRewards");
						for(int k = 0; k < itemArray.size(); k++)
						{
							//Add each item to array (items should be paths to JSON definitions)
							//tempReward.itemRewards.add(new Item(itemArray.get("definitionPath").asText()));
							tempReward.itemRewards.add(itemFactory.getItem(itemArray.get("definitionPath").asText()));
						}
						//Add to step reward list
						encounterSteps[i].rewards.put(optionIndex, tempReward);
					}
				break;
				//Combat step
				case 1:
					int enemyIndex = tempNode.get("enemyIndex").asInt();
					int nextStepIndex;
					try {
						nextStepIndex = tempNode.get("nextStepIndex").asInt();
					}
					catch (Exception e)
					{
						nextStepIndex = -1;
					}
					encounterSteps[i] = new CombatStep(enemyIndex, nextStepIndex);
					encounterSteps[i].parentEncounter = this;
					try {
						encounterSteps[i].backgroundImagePath = tempNode.get("backgroundImagePath").asText();
					}
					catch(NullPointerException e)
					{
						encounterSteps[i].backgroundImagePath = "";
					}
					//Add reward
					rewardArray = tempNode.get("rewards");
					if(rewardArray != null)
					{
						JsonNode reward = rewardArray.get(0);
						Encounter.Reward tempReward = new Encounter.Reward();
						try {
							tempReward.experienceReward = reward.get("experienceReward").asInt();
						}
						catch(Exception e)
						{
							tempReward.experienceReward = 0;
						}
						try {
							tempReward.goldReward = reward.get("goldReward").asInt();
						}
						catch(Exception e)
						{
							tempReward.goldReward = 0;
						}
						JsonNode itemArray = reward.get("itemRewards");
						if(itemArray != null)
						{
							for (int k = 0; k < itemArray.size(); k++) {
								//Add each item to array (items should be paths to JSON definitions)
								//tempReward.itemRewards.add(new Item(itemArray.get("definitionPath").asText()));
								tempReward.itemRewards.add(itemFactory.getItem(itemArray.get(k).get("definitionPath").asText()));
							}
						}
						encounterSteps[i].rewards.put(0, tempReward);
					}

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
			entityArray[i].imagePath = tempNode.get("imageForeground").asText();
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

		public int figureDamage()
		{
			int output = 0;
			//Defaulting to using strength unless combat rules become more complex later
			output += Dice.d8(1) + strength;
			return output;
		}

		public void applyDamage(int damage)
		{
			if(health >= damage)
				health -= damage;
			else
				health = 0;
		}
		
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
			for(int i = 0; i < itemRewards.size(); i++)
			{
				player.playerCharacter.addItem(itemRewards.get(i));
			}
			//Update player in database
				//Set playerUpdated flag
				playerUpdated = true;
		}
	}
}
