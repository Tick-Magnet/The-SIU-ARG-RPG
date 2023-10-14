package com.RPGServer;

import com.RPGServer.ItemSystem.Item;
import com.RPGServer.ItemSystem.ItemFactory;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.*;
import jakarta.annotation.*;
import org.apache.catalina.User;

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
	private Item[][] inventory;
	
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
		this.inventory = new Item[5][5];
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
		int armorBonus = 0;
		//Figure armorBonus from equipped items

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

	public boolean addItem(Item newItem)
	{
		boolean output = false;
		//if it is a resource item, attempt to stack it with others
		if(newItem.itemType == Item.ItemType.RESOURCE)
		{
			ItemFactory.ResourceItem newResource = (ItemFactory.ResourceItem)newItem;
			for(int i = 0; i < inventory.length; i++)
			{
				for(int j = 0; j < inventory[0].length; j++)
				{
					if(inventory[i][j].name.equals(newItem.name) && inventory[i][j].itemType == Item.ItemType.RESOURCE)
					{
						ItemFactory.ResourceItem oldResource = (ItemFactory.ResourceItem)inventory[i][j];
						if(oldResource.stackSize + newResource.stackSize <= oldResource.maxStackSize)
						{
							//Add stack sizes
							oldResource.stackSize += newResource.stackSize;
							return true;
						}
					}
				}
			}
		}
		//Attempt to find an empty slot
		else
		{
			for(int i = 0; i < inventory.length; i++)
			{
				for(int j = 0; j < inventory.length; j++)
				{
					if(inventory[i][j] == null)
					{
						inventory[i][j] = newItem;
						return true;
					}
				}
			}
		}
		return output;
	}
	public Item removeItem(int row, int column)
	{
		Item output = null;
		if(row >= 0 && row < inventory.length && column >= 0 && column < inventory[0].length)
		{
			output = inventory[row][column];
			inventory[row][column] = null;
		}
		return output;
	}
	public boolean swapItem(int item1Row, int item1Column, int item2Row, int item2Column)
	{
		boolean output = false;
		if(item1Row >= 0 && item1Row < inventory.length && item1Column >= 0 && item1Column < inventory[0].length)
		{
			if(item2Row >= 0 && item2Row < inventory.length && item2Column >= 0 && item2Column < inventory[0].length)
			{
				//If item is swapping into rows 2 - 5, just swap
				if(item1Row > 0)
				{
					Item tempItem = inventory[item1Row][item1Column];
					inventory[item1Row][item1Column] = inventory[item2Row][item2Column];
					inventory[item2Row][item2Column] = tempItem;
					output = true;
				}
			}
			else
			{
				switch(item1Column)
				{
					//weapon slot
					case 0:
						if(inventory[item2Row][item2Column] == null || inventory[item2Row][item2Column].itemType == Item.ItemType.WEAPON)
						{
							//check that item matches characters weapon type
							ItemFactory.Weapon weapon = (ItemFactory.Weapon)inventory[item2Row][item2Column];
							if(weapon.weaponType == characterType.weaponType || weapon.weaponType == CharacterType.WeaponType.SPECIAL)
							{
								//Allow swap to occur
								Item tempItem = inventory[item1Row][item1Column];
								inventory[item1Row][item1Column] = inventory[item2Row][item2Column];
								inventory[item2Row][item2Column] = tempItem;
								output = true;
							}
						}
						break;
					//Helmet slot
					case 1:
						if(inventory[item2Row][item2Column] == null || inventory[item2Row][item2Column].itemType == Item.ItemType.HELMET)
						{
							//Allow swap to occur
							Item tempItem = inventory[item1Row][item1Column];
							inventory[item1Row][item1Column] = inventory[item2Row][item2Column];
							inventory[item2Row][item2Column] = tempItem;
							output = true;
						}
						break;
					//Chest slot
					case 2:
						if(inventory[item2Row][item2Column] == null || inventory[item2Row][item2Column].itemType == Item.ItemType.CHEST_ARMOR)
						{
							//Allow swap to occur
							Item tempItem = inventory[item1Row][item1Column];
							inventory[item1Row][item1Column] = inventory[item2Row][item2Column];
							inventory[item2Row][item2Column] = tempItem;
							output = true;
						}
						break;
					//Leg slot
					case 3:
						if(inventory[item2Row][item2Column] == null || inventory[item2Row][item2Column].itemType == Item.ItemType.LEG_ARMOR)
						{
							//Allow swap to occur
							Item tempItem = inventory[item1Row][item1Column];
							inventory[item1Row][item1Column] = inventory[item2Row][item2Column];
							inventory[item2Row][item2Column] = tempItem;
							output = true;
						}
						break;
					//Boot slot
					case 4:
						if(inventory[item2Row][item2Column] == null || inventory[item2Row][item2Column].itemType == Item.ItemType.BOOTS)
						{
							//Allow swap to occur
							Item tempItem = inventory[item1Row][item1Column];
							inventory[item1Row][item1Column] = inventory[item2Row][item2Column];
							inventory[item2Row][item2Column] = tempItem;
							output = true;
						}
						break;
					default:
						break;
				}
			}
		}
		return output;
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
