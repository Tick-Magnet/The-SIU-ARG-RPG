package com.RPGServer;

import com.RPGServer.ItemSystem.Item;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Entity
public class QRCodeEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer Id;
		
	public UUID uuid;
	
	public int type;
	
	//Array containing awards for redeeming
	//public Item[] items;
	//gold reward for item QR codes
	public int goldReward;
	//Experience rewards for item QR codes
	public int experienceReward;
	
	//Path to encounter definition file
	public String encounterDefinitionPath;
	public String itemDefinitionPath;


}
