package com.RPGServer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.*;
import jakarta.annotation.*;

@Embeddable
//@Entity
public class PlayerCharacter
{
	public static final int BASE_HEALTH = 20;

	public CharacterType characterType;
	public int experience;
	public int gold;
	public int level;

	public boolean creationComplete;
	public boolean statsRolled;
	private int health;
	private int strength;
	private int dexterity;
	private int constitution;
	private int intelligence;
	
	@Nullable
	private int weaponModifier;
	@Nullable
	private int armorModifier;

	
	public PlayerCharacter()
	{
		strength = 0;
		dexterity = 0;
		intelligence = 0;
		constitution = 0;;
		health = 0;
		//Maybe change to 1 later
		level = 0;
		gold = 0;
		experience = 0;
		weaponModifier = 0;
		armorModifier = 0;
		
		setCharacterType(new CharacterType(CharacterType.CharacterClass.KNIGHT, CharacterType.CharacterRace.HUMAN));
		statsRolled = false;
		
		creationComplete = false;
	}
	
	public void rollStats()
	{
		//Generate stats with random rolls
		//Using sum of 4 d6 rolls
		strength = Dice.d6(3);
		dexterity = Dice.d6(3);
		intelligence = Dice.d6(3);
		constitution = Dice.d6(3);
		health = constitution + BASE_HEALTH;
		//Maybe change to 1 later
		level = 0;
		gold = 0;
		experience = 0;
		statsRolled = true;
		
		//User will be able to select race and class after they roll their stats
		creationComplete = false;
	}

	public boolean setCharacterType(CharacterType type)
	{
		if(creationComplete != true)
		{
			this.characterType = type;
			creationComplete = true;
			//Apply stat modifiers
			strength += characterType.strengthModifier;
			dexterity += characterType.dexterityModifier;
			intelligence += characterType.intelligenceModifier;
			constitution += characterType.constitutionModifier;
			resetHealth();

			return true;
		}
		else
		{
			return false;
		}
	}

	public void levelUp()
	{
		//Check how many stat gains need to be applied
		int levelsGained = (int)Math.floor(Math.sqrt(experience)) - level;
		//Apply stat gains and increase level
		//Modify attack stat (+2 points)
		//Modify weak stat (+1 point every other level up
		//Modify other stats (+1 point)
		strength += figureStatIncrease(CharacterType.Stat.STRENGTH);
		dexterity += figureStatIncrease(CharacterType.Stat.DEXTERITY);
		intelligence += figureStatIncrease(CharacterType.Stat.INTELLIGENCE);
		constitution += figureStatIncrease(CharacterType.Stat.CONSTITUTION);
	}
	//Function to return stat increases based on the character type enum values
	private int figureStatIncrease(CharacterType.Stat currentStat)
	{
		int output = 0;
		//Attack stat case
		if(this.characterType.attackStat == currentStat)
		{
			output = 2;
		}
		//Weak stat case
		else if(this.characterType.weakStat == currentStat)
		{
			if(this.level % 2 == 1)
			{
				output = 1;
			}
		}
		else
		{
			output = 1;
		}
		//other stat case
		return output;
	}
	//Add experience returning true if a level up occured
	public boolean applyExperience(int xp)
	{
		boolean output = false;
		experience += xp;
		if(experience >= Math.pow((level + 1), 2))
		{
			output = true;
			levelUp();
		}
		return output;
	}
	//Reset health back to full, should be used after a stat change is applied
	public void resetHealth()
	{
		health = BASE_HEALTH + constitution;
	}

	public int figureDamage()
	{
		int damage = 0;
		//Add D8 dice roll
		damage += Dice.d8(1);
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
