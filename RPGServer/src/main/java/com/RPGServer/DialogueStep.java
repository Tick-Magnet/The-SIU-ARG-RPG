package com.RPGServer;


public class DialogueStep implements EncounterStep
{
	//Return next EncounterStep depending on the selected dialogue option
	public EncounterStep nextStep()
	{
		return null;
	}
	//Update Entities in the encounter or grant rewards
	public void endStep()
	{
		
	}
	//Should select appropriate next step and return its initial StepUpdate. Also update the current step in the encounter object
	public StepUpdate postStepUpdate(StepUpdate update)
	{
		
		return null;
	}
	
	public StepUpdate getInitialStepUpdate()
	{
		return null;
	}
}
