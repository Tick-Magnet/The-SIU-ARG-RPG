package com.RPGServer.RESTControllers;

import com.RPGServer.*;
import com.RPGServer.EncounterSystem.Encounter;
import com.RPGServer.ItemSystem.Item;
import com.RPGServer.ItemSystem.ItemFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.UUID;

import java.io.IOException;

import java.util.Map;



@RestController
public class QRCodeRedeemController
{
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@Autowired
	private QRCodeRepository qrCodeRepository;
	
	//Using UUID for QR code token, not as secure but easier to include in a URL
	@PostMapping("/redeemQR")
	public Map<String,Object> redeem(@RequestBody Map<String, Object> payload)
	{
		//Read JSON values from HTTP payload
		String username = (String)payload.get("username");
		UUID uuid = UUID.fromString((String)payload.get("uuid"));
		String sessionToken = (String)payload.get("sessionToken");

		HashMap<String, Object> result = new HashMap<String,Object>();
		//Retrieve user account by username
		UserAccount account = userAccountRepository.findByUsername(username);
		//Verify account session token
		if(account.isValidSessionToken(sessionToken) && account.playerCharacter.creationComplete == true)
		{
			
			//Look up QR code by UUID in database
			QRCodeEntity qrCode = qrCodeRepository.findByuuid(uuid);
			//If QR Code exists, then redeem
			if(qrCode != null)
			{
				//Handle QR code depending on QR code type
				switch (qrCode.type)
				{
					//Item reward
					case 0:
						ItemFactory itemFactory = new ItemFactory();
						//Grant player account the item reward
						//Create new item
						try
						{
							Item tempItem = itemFactory.getItem(qrCode.itemDefinitionPath);
							result.put("type", 0);
							result.put("item", tempItem);
							result.put("message", "Item redeemed");
							result.put("goldReward", qrCode.goldReward);
							result.put("experienceReward", qrCode.experienceReward);
							account.playerCharacter.applyExperience(qrCode.experienceReward);
							account.playerCharacter.gold += qrCode.goldReward;
							account.playerCharacter.addItem(tempItem);
							userAccountRepository.save(account);
						}
						catch (IOException e)
						{
							e.printStackTrace();
							result.put("message", e.toString());
						}
					break;
					//Encounter
					case 1:
						try
						{
							//Create encounter
							Encounter tempEncounter = new Encounter(account, qrCode.encounterDefinitionPath);
							RpgServerApplication.encounterMap.put(tempEncounter.uuid, tempEncounter);

							result.put("type",1);
							result.put("message", "Created encounter");
							result.put("encounterID", tempEncounter.uuid);
						}
						catch(IOException e)
						{
							e.printStackTrace();
						}
						break;

					default:
					
					break;
				}
			}
		}
		return result;
	}
}
