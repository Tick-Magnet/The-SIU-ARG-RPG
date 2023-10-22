package com.RPGServer.EncounterSystem;

import java.util.HashMap;
import java.util.Map;

public class StepUpdate
{
	public String[] choices;
	
	public int stepType;
	public int selectedChoice;
	public String backgroundImagePath;

	public StepUpdate(Map<String, Object> payload)
	{
		selectedChoice = (int)payload.get("selectedChoice");
	}
	public StepUpdate()
	{
	}

	public Map<String, Object> toMap()
	{
		HashMap<String, Object> output = new HashMap<String, Object>();
		output.put("choices", choices);
		output.put("stepType", stepType);
		output.put("selectedChoice", selectedChoice);
		output.put("backgroundImagePath", backgroundImagePath);
		return output;
	}
	
}
