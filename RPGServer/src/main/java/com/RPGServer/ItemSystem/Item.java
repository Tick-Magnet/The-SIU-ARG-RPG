package com.RPGServer.ItemSystem;


import com.fasterxml.jackson.databind.JsonNode;

public abstract class Item
{
    public String name;
    public ItemType itemType;
    public String itemDescription;
    public boolean canEquip;
    public ItemGrade itemGrade;
    public int goldValue;
    public String imagePath;

    public Item(JsonNode jsonNode)
    {
        this.name = jsonNode.get("name").asText();
        this.itemType = ItemType.valueOf(jsonNode.get("itemType").asText());
        this.itemDescription = jsonNode.get("itemDescription").asText();
        this.canEquip = jsonNode.get("canEquip").asBoolean();
        this.itemGrade = ItemGrade.valueOf(jsonNode.get("itemGrade").asText());
        this.goldValue = jsonNode.get("goldValue").asInt();
        this.imagePath = jsonNode.get("imagePath").asText();
    }
    public enum ItemType
    {
        WEAPON,
        CHEST_ARMOR,
        LEG_ARMOR,
        BOOTS,
        HELMET,
        RESOURCE
    }

    public enum ItemGrade
    {
        WOOD,
        STONE,
        BRONZE,
        IRON,
        STEEL,
        MAGIC
    }
}