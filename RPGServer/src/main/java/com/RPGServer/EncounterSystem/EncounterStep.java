package com.RPGServer.EncounterSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class EncounterStep
{
	public Encounter parentEncounter;

	//Should contain a reward object, selected choice should correspond to the index of the reward
	//Index does not matter for combat steps, all rewards granted on completion of step
	public HashMap<Integer, Encounter.Reward> rewards;
	
	public int stepType;
	//Actions that should be completed upon completion of this step
	public abstract void endStep(int selectedChoice);
	//Merge StepUpdate from client to state of current step
	public abstract StepUpdate postStepUpdate(StepUpdate update);
	//Return initial step update to begin step for client
	public abstract StepUpdate getInitialStepUpdate();

	
}
