package com.RPGServer;

import java.util.ArrayList;

public class DialogueStep extends EncounterStep
{
	private ArrayList<DialogueOption> dialogueOptions;
	private int nextStep;
	private Encounter parentEncounter;
	
	public DialogueStep()
	{
		dialogueOptions = new ArrayList<DialogueOption>();
	}
	
	public void addDialogueOption(String text, int nextStep)
	{
		dialogueOptions.add(new DialogueOption(text, nextStep));
	}
	
	//Update Entities in the encounter or grant rewards
	public void endStep()
	{
		//Grant reward
	}
	//Should select appropriate next step and return its initial StepUpdate. Also update the current step in the encounter object
	public StepUpdate postStepUpdate(StepUpdate update)
	{
		StepUpdate output = null;
		//Check if choice is in range and select next step
		if(update.selectedChoice >= 0 && update.selectedChoice < dialogueOptions.size())
		{
			//Update parent encounter's current step to the proper next step and get its initial stateupdate
			parentEncounter.currentStep = parentEncounter.encounterSteps[dialogueOptions.get(update.selectedChoice).nextStep];
			output = parentEncounter.currentStep.getInitialStepUpdate();
		}
		return output;
	}
	
	public StepUpdate getInitialStepUpdate()
	{
		StepUpdate update = new StepUpdate();
		update.choices = (String[])dialogueOptions.toArray();
		update.selectedChoice = -1;
		update.stepType = 0;
		
		return update;
	}
	
	private class DialogueOption
	{
		public String text;
		public int nextStep;
		
		public DialogueOption(String text, int nextStep)
		{
			this.text = text;
			this.nextStep = nextStep;
		}
	}
	
	@Override
	public String toString()
	{
		String output = "";
		for(int i = 0; i < dialogueOptions.size(); i++)
		{
			output = output + dialogueOptions.get(i).text;
		}
		
		return output;
	}
}
