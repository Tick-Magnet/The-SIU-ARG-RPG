package com.RPGServer;

import java.util.Random;

//Static functions for returning dice rolls
public class Dice
{
	private static Random randomGenerator = new Random();
	
	public static int d6(int rolls)
	{
		int output = 0;
		for(int i = 0; i < rolls; i++, output += randomGenerator.nextInt(6)+1);
		return output;
	}
	
	public static int d8(int rolls)
	{
		int output = 0;
		for(int i = 0; i < rolls; i++, output += randomGenerator.nextInt(8)+1);
		return output;
	}
	
	public static int d4(int rolls)
	{
		int output = 0;
		for(int i = 0; i < rolls; i++, output += randomGenerator.nextInt(4)+1);
		return output;
	}
	
	public static int d20(int rolls)
	{
		int output = 0;
		for(int i = 0; i < rolls; i++, output += randomGenerator.nextInt(20)+1);
		return output;
	}
}
