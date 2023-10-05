package com.RPGServer;

import jakarta.persistence.Embeddable;

@Embeddable
public class CharacterType
{
	
	public int strengthModifier;
	public int dexterityModifier;
	public int constitutionModifier;
	public int intelligenceModifier;
	
	public Stat attackStat;
	public Stat weakStat;
	public WeaponType weaponType;
	//Default constructor required by jakarta, ideally should not be used
	public CharacterType()
	{

	}
	public CharacterType(CharacterClass characterClass, CharacterRace characterRace)
	{
		switch(characterClass)
		{
			case BARBARIAN:
				weaponType = WeaponType.AXE;
				attackStat = Stat.STRENGTH;
				weakStat = Stat.INTELLIGENCE;
				break;
			case KNIGHT:
				weaponType = WeaponType.SWORD;
				attackStat = Stat.CONSTITUTION;
				weakStat = Stat.DEXTERITY;
				break;
			case ROGUE:
				weaponType = WeaponType.BOW;
				attackStat = Stat.DEXTERITY;
				weakStat = Stat.STRENGTH;
				break;
			case WIZARD:
				weaponType = WeaponType.WAND;
				attackStat = Stat.INTELLIGENCE;
				weakStat = Stat.CONSTITUTION;
				break;
			default:
				break;
		}
		switch(characterRace)
		{
			case HUMAN:
				strengthModifier = 0;
				dexterityModifier = 0;
				constitutionModifier = 0;
				intelligenceModifier = 0;
				break;
			case ELF:
				strengthModifier = -1;
				dexterityModifier = 1;
				constitutionModifier = 0;
				intelligenceModifier = 1;
				break;
			case DWARF:
				strengthModifier = 1;
				dexterityModifier = -1;
				constitutionModifier = 1;
				intelligenceModifier = 0;
				break;
			default:
				break;
		}
	}
	
	public enum CharacterClass
	{
		BARBARIAN,
		KNIGHT,
		ROGUE,
		WIZARD;
	}
	
	public enum CharacterRace
	{
		HUMAN,
		ELF,
		DWARF;
	}
	
	public enum Stat
	{
		STRENGTH,
		DEXTERITY,
		CONSTITUTION,
		INTELLIGENCE;	
	}
	
	public enum WeaponType
	{
		SWORD,
		AXE,
		BOW,
		WAND;
	}
}
