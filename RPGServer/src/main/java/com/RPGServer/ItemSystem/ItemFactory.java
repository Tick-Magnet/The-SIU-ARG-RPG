package com.RPGServer.ItemSystem;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
        public Weapon(JsonNode jsonNode)
        {

        }
    }
    public class ChestArmor extends Item
    {
        public ChestArmor(JsonNode jsonNode)
        {

        }
    }
    public class LegArmor extends Item
    {
        public LegArmor(JsonNode jsonNode)
        {

        }
    }
    public class Boots extends Item
    {
        public Boots(JsonNode jsonNode)
        {

        }
    }
    public class Helmet extends Item
    {
        public Helmet(JsonNode jsonNode)
        {

        }
    }
    public class ResourceItem extends Item
    {
        public ResourceItem(JsonNode jsonNode)
        {

        }
    }
}
