package com.RPGServer;

import java.util.Random;

//Static functions for returning dice rolls
public class Dice
{
	private static Random randomGenerator = new Random();
	
	public static int d6()
	{
		return randomGenerator.nextInt(6) + 1;
	}
	
	public static int d8()
	{
		//Excludes 8, adding 1 to exclude 0;
		return randomGenerator.nextInt(8) + 1;
	}
	
	public static int d4()
	{
		return randomGenerator.nextInt(4) + 1;
	}
	
	public static int d20()
	{
		return randomGenerator.nextInt(20) + 1;
	}
}
