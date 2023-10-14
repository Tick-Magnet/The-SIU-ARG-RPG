package com.RPGServer.ItemSystem;


public abstract class Item
{
    public String name;
    public ItemType itemType;
    public boolean canEquip;
    public ItemGrade itemGrade;

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