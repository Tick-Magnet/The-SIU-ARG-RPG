package com.RPGServer.EncounterSystem;

import com.RPGServer.Dice;
import com.RPGServer.PlayerCharacter;
import com.RPGServer.UserAccount;
import com.RPGServer.UserAccountRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CombatStep extends EncounterStep
{
	private int enemyIndex;
	private int nextStepIndex;
	private boolean stepWon;

	
	public CombatStep(int enemyIndex, int nextStepIndex)
	{
		this.enemyIndex = enemyIndex;
		this.nextStepIndex = nextStepIndex;
		this.rewards = new HashMap<Integer, Encounter.Reward>();
		stepWon = false;
	}	
	
	@Override
	public void endStep(int selectedChoice)
	{
		//Grant reward
		if(rewards.get(0) != null && stepWon)
		{
			parentEncounter.encounterRewards.add(rewards.get(0));
		}
	}
	
	@Override
	public StepUpdate postStepUpdate(StepUpdate update)
	{
		Encounter.EncounterEntity enemy = parentEncounter.entityArray[enemyIndex];
		PlayerCharacter player = parentEncounter.playerAccount.playerCharacter;
		StepUpdate output;
		
		CombatStepUpdate combatUpdate = new CombatStepUpdate();
		combatUpdate.enemyName = enemy.name;
		combatUpdate.enemyImagePath = enemy.imagePath;
		combatUpdate.backgroundImagePath = backgroundImagePath;
		combatUpdate.choices = new String[]{"attack", "leave"};
		//Process step update
		switch(update.selectedChoice)
		{
			//Player attacks
			case 0:
				//Figure damage using players character
				enemy.applyDamage(player.figureDamage());
			break;
			//Leave encounter
			case 1:
				//End step, signal end encounter
				endStep(0);
				parentEncounter.nextEncounterStep(-1);
			break;
			
			default:
			break;
		}
		
		//Process entity's attack
		if(enemy.health > 0)
		{
			player.applyDamage(enemy.figureDamage());
		}
		//Figure damage from entities stats
		combatUpdate.enemyHealth = enemy.health;
		combatUpdate.playerHealth = player.getHealth();

		//If player health is zero, end encounter
		if(player.getHealth() <= 0)
		{
			parentEncounter.endEncounter();
		}
		//If enemy health is zero, end step
		//Return next initial step
		if(enemy.health <= 0)
		{
			stepWon = true;
			endStep(0);
			//parentEncounter.currentStep = parentEncounter.encounterSteps[nextStepIndex];
			parentEncounter.nextEncounterStep(nextStepIndex);
			output = parentEncounter.currentStep.getInitialStepUpdate();
		}
		else
		{
			output = combatUpdate;
		}
		return output;
	}


	
	@Override
	public StepUpdate getInitialStepUpdate()
	{
		CombatStepUpdate output = new CombatStepUpdate();
		output.playerHealth = parentEncounter.playerAccount.playerCharacter.getHealth();
		output.enemyHealth = parentEncounter.entityArray[enemyIndex].health;
		output.stepType = 1;
		output.choices = new String[]{"attack", "leave"};
		Encounter.EncounterEntity enemy = parentEncounter.entityArray[enemyIndex];
		output.enemyName = enemy.name;
		output.enemyImagePath = enemy.imagePath;
		output.backgroundImagePath = backgroundImagePath;
		return output;
	}
	
	public class CombatStepUpdate extends StepUpdate
	{
		int playerHealth;
		int enemyHealth;
		String enemyName;
		String enemyImagePath;
		String backgroundImagePath;
		@Override
		public Map<String, Object> toMap()
		{
			Map<String, Object> output = super.toMap();
			//Add each relevant field to map
			output.put("playerHealth", playerHealth);
			output.put("enemyHealth", enemyHealth);
			output.put("enemyName", enemyName);
			output.put("enemyImagePath", enemyImagePath);
			output.put("backgroundImagePath", backgroundImagePath);
			return output;
		}
	}
}
