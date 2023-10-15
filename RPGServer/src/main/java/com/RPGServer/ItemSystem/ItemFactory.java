package com.RPGServer.ItemSystem;

import com.RPGServer.CharacterType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Embeddable;

import java.io.*;

public class ItemFactory
{
    //Reads JSON definitions stored and returns a subclass of Item based on the definition file
    public Item getItem(String definitionPath) throws IOException
    {
        ObjectMapper objectMapper = new ObjectMapper();

        //Read JSON file from resource folder
        //InputStream inputStream = getClass().getResourceAsStream(definitionPath);
        InputStream inputStream = getClass().getResourceAsStream(definitionPath);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JsonNode rootNode = objectMapper.readTree(inputStream);

        try
        {
            Item.ItemType itemType = Item.ItemType.valueOf(rootNode.get("itemType").asText());
            switch(itemType)
            {
                case WEAPON:
                    return new Weapon(rootNode);
                case CHEST_ARMOR:
                    return new ChestArmor(rootNode);
                case LEG_ARMOR:
                    return new LegArmor(rootNode);
                case BOOTS:
                    return new Boots(rootNode);
                case HELMET:
                    return new Helmet(rootNode);
                case RESOURCE:
                    return new ResourceItem(rootNode);
            }
        }
        catch(IllegalArgumentException exception)
        {
            return null;
        }
        return null;
    }
    public class Weapon extends Item
    {
        public CharacterType.WeaponType weaponType;
        public int attackModifier;
        public Weapon(JsonNode jsonNode)
        {
            super(jsonNode);
            this.attackModifier = jsonNode.get("attackModifier").asInt();
            this.weaponType = CharacterType.WeaponType.valueOf(jsonNode.get("weaponType").asText());
            this.canEquip = true;
        }
    }
    public abstract class ArmorItem extends Item
    {
        int armorBonus;
        public ArmorItem(JsonNode jsonNode)
        {
            super(jsonNode);
            this.canEquip = true;
            this.armorBonus = jsonNode.get("armorBonus").asInt();
        }
    }
    public class ChestArmor extends ArmorItem
    {
        public ChestArmor(JsonNode jsonNode)
        {
            super(jsonNode);
        }
    }
    public class LegArmor extends ArmorItem
    {
        public LegArmor(JsonNode jsonNode)
        {
            super(jsonNode);
        }
    }
    public class Boots extends ArmorItem
    {
        public Boots(JsonNode jsonNode)
        {
            super(jsonNode);
        }
    }
    public class Helmet extends ArmorItem
    {
        public Helmet(JsonNode jsonNode)
        {
            super(jsonNode);
        }
    }
    public class ResourceItem extends Item
    {
        public int stackSize;
        public int maxStackSize;
        public ResourceItem(JsonNode jsonNode)
        {
            super(jsonNode);
            this.stackSize = jsonNode.get("stackSize").asInt();
            this.maxStackSize = jsonNode.get("maxStackSize").asInt();
        }
    }
}
