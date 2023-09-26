package com.RPGServer;

import java.util.ArrayList;

public class DialogueStep implements EncounterStep
{
	private ArrayList<DialogueOption> dialogueOptions;
	
	public DialogueStep()
	{
		dialogueOptions = new ArrayList<DialogueOption>();
	}
	
	public void addDialogueOption(String text, int nextStep)
	{
		dialogueOptions.add(new DialogueOption(text, nextStep));
	}
	
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
}
