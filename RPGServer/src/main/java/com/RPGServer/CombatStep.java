package com.RPGServer;

import java.util.ArrayList;

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
		CombatStepUpdate output = new CombatStepUpdate();
		//Process step update
		switch(update.selectedChoice)
		{
			//Player attacks
			case 0:
			//Figure damage using players character
			
			break;
			//Use item 
			case 1:
			
			break;
			
			default:
			break;
		}
		
		//Process entity's attack
		//Figure damage from entities stats
		
		//cast input to combat step update
		CombatStepUpdate input = (CombatStepUpdate)update;
		
		return output;
	}
	
	@Override
	public StepUpdate getInitialStepUpdate()
	{
		CombatStepUpdate output = new CombatStepUpdate();
		output.playerHealth = 0;
		output.enemyHealth = parentEncounter.entityArray[enemyIndex].health;
		
		return output;
	}
	
	public class CombatStepUpdate extends StepUpdate
	{
		int playerHealth;
		int enemyHealth;
	}
}
