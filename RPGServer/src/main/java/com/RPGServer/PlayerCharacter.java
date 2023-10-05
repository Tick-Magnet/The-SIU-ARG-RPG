package com.RPGServer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import jakarta.persistence.Embeddable;

@Embeddable
//@Entity
public class PlayerCharacter
{
	public CharacterType characterType;
	private int health;
	private int strength;
	private int dexterity;
	private int constitution;
	private int intelligence;
	
	private int weaponModifier;
	private int armorModifier;
	
	/*
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer Id;
	*/
	
	public PlayerCharacter(int strength, int dexterity, int constitution, int intelligence, CharacterType characterType)
	{
		this.strength = strength;
		this.dexterity = dexterity;
		this.constitution = constitution;
		this.intelligence = intelligence;
		this.characterType = characterType;
	}
	
	public int figureDamage()
	{
		int damage = 0;
		//Add D8 dice roll
		damage += Dice.d8();
		//Add attack stat
		switch (characterType.attackStat)
		{
			case STRENGTH:
				damage += strength;
			break;
			case DEXTERITY:
				damage += dexterity;
			break;
			case INTELLIGENCE:
				damage += intelligence;
			break;
			case CONSTITUTION:
				damage += constitution;
			break;
			default:
			break;
		}
		//Add weapon damage
		damage += weaponModifier;
		
		return damage;
	}
	
	//Apply damage or negative number for healing
	public int applyDamage(int damage)
	{
		//Apply damage, not allowing health to be negative
		if(health >= damage)
		{
			health -= damage;
		}
		else
		{
			health = 0;
		}
		
		return health;
	}
	
	public int getHealth()
	{
		return health;
	}
	public int getStrength()
	{
		return strength;
	}
	public int getDexterity()
	{
		return dexterity;
	}
	public int getConstitution()
	{
		return constitution;
	}
	public int getIntelligence()
	{
		return intelligence;
	}
}
