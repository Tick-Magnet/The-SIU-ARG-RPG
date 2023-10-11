package com.RPGServer.EncounterSystem;

import com.RPGServer.Dice;
import com.RPGServer.PlayerCharacter;
import com.RPGServer.UserAccount;
import com.RPGServer.UserAccountRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class CombatStep extends EncounterStep
{
	private int enemyIndex;
	private int nextStepIndex;
	
	public CombatStep(int enemyIndex, int nextStepIndex)
	{
		this.enemyIndex = enemyIndex;
		this.nextStepIndex = nextStepIndex;
	}	
	
	@Override
	public void endStep()
	{
		//Grant reward
	}
	
	@Override
	public StepUpdate postStepUpdate(StepUpdate update)
	{
		Encounter.EncounterEntity enemy = parentEncounter.entityArray[enemyIndex];
		PlayerCharacter player = parentEncounter.playerAccount.playerCharacter;

		CombatStepUpdate output = new CombatStepUpdate();
		output.choices = new String[]{"attack", "leave"};
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
			break;
			
			default:
			break;
		}
		
		//Process entity's attack
		player.applyDamage(enemy.figureDamage());
		//Figure damage from entities stats
		output.enemyHealth = enemy.health;
		output.playerHealth = player.getHealth();

		
		return output;
	}


	
	@Override
	public StepUpdate getInitialStepUpdate()
	{
		CombatStepUpdate output = new CombatStepUpdate();
		output.playerHealth = 0;
		output.enemyHealth = parentEncounter.entityArray[enemyIndex].health;
		output.stepType = 1;
		return output;
	}
	
	public class CombatStepUpdate extends StepUpdate
	{
		int playerHealth;
		int enemyHealth;
		@Override
		public Map<String, Object> toMap()
		{
			Map<String, Object> output = super.toMap();
			//Add each relevant field to map
			output.put("playerHealth", playerHealth);
			output.put("enemyHealth", enemyHealth);

			return output;
		}
	}
}
