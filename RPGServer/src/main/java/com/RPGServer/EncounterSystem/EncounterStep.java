package com.RPGServer.EncounterSystem;


public abstract class EncounterStep
{
	public Encounter parentEncounter;
	//Actions that should be completed upon completion of this step
	public abstract void endStep();
	//Merge StepUpdate from client to state of current step
	public abstract StepUpdate postStepUpdate(StepUpdate update);
	//Return initial step update to begin step for client
	public abstract StepUpdate getInitialStepUpdate();
}
