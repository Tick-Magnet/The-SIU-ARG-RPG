package com.RPGServer;


public interface EncounterStep
{
	//Should return a reference to the next encounter step
	public EncounterStep nextStep();
	//Actions that should be completed upon completion of this step
	public void endStep();
	//Merge StepUpdate from client to state of current step
	public StepUpdate postStepUpdate(StepUpdate update);
	//Return initial step update to begin step for client
	public StepUpdate getInitialStepUpdate();
}
