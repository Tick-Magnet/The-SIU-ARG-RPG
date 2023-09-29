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
		
	}
	
	@Override
	public StepUpdate postStepUpdate(StepUpdate update)
	{
		CombatStepUpdate output = new CombatStepUpdate();
		//Process step update
		//cast input to combat step update
		CombatStepUpdate input = (CombatStepUpdate)update;
		
		return output;
	}
	
	@Override
	public StepUpdate getInitialStepUpdate()
	{
		CombatStepUpdate output = new CombatStepUpdate();
		
		
		return output;
	}
	
	public class CombatStepUpdate extends StepUpdate
	{
		int playerHealth;
		int enemyHealth;
	}
}
