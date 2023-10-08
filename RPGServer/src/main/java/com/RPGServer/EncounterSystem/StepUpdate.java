package com.RPGServer.EncounterSystem;

import java.util.Map;

public class StepUpdate
{
	public String[] choices;
	public int stepType;
	public int selectedChoice;

	public StepUpdate(Map<String, Object> payload)
	{
		selectedChoice = (int)payload.get("selectedChoice");
	}
	public StepUpdate()
	{
	}
}
