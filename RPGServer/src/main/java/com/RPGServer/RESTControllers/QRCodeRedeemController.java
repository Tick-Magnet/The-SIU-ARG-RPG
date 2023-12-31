package com.RPGServer.RESTControllers;

import com.RPGServer.*;
import com.RPGServer.EncounterSystem.Encounter;
import com.RPGServer.ItemSystem.Item;
import com.RPGServer.ItemSystem.ItemFactory;
import com.RPGServer.Security.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
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
	@Autowired
	private RateLimitService rateLimitService;
	
	//Using UUID for QR code token, not as secure but easier to include in a URL
	@PostMapping("/redeemQR")
	public Map<String,Object> redeem(@RequestBody Map<String, Object> payload, HttpServletRequest request)
	{
		HashMap<String, Object> result = new HashMap<String,Object>();
		if(rateLimitService.filterIP(request.getRemoteAddr(),RateLimitService.CallType.NORMAL) == false)
		{
			result.put("message", "Blocked for exceeding API limits");
			return result;
		}

		//Read JSON values from HTTP payload
		String username = (String)payload.get("username");
		UUID uuid = UUID.fromString((String)payload.get("uuid"));
		String sessionToken = (String)payload.get("sessionToken");


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
						try {
							if (qrCode.itemDefinitionPath != null)
							{
								Item tempItem = itemFactory.getItem(qrCode.itemDefinitionPath);
								result.put("item", tempItem);
								account.playerCharacter.addItem(tempItem);
							}
							result.put("type", 0);

							result.put("message", "Item redeemed");
							result.put("goldReward", qrCode.goldReward);
							result.put("experienceReward", qrCode.experienceReward);
							result.put("backgroundImagePath", qrCode.backgroundImagePath);
							account.playerCharacter.applyExperience(qrCode.experienceReward);
							account.playerCharacter.gold += qrCode.goldReward;

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
					case 2:
						account.playerCharacter.resetHealth();
						userAccountRepository.save(account);
						result.put("type", 2);
						result.put("message", "Player healed to full health");
						result.put("backgroundImagePath", qrCode.backgroundImagePath);

						break;
					default:

					break;
				}
			}
		}
		return result;
	}
}
