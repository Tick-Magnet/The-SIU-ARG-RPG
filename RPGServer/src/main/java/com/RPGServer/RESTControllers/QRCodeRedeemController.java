package com.RPGServer.RESTControllers;

import com.RPGServer.*;
import com.RPGServer.EncounterSystem.Encounter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

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
	public QRRedeemResult redeem(@RequestBody Map<String, Object> payload)
	{
		//Read JSON values from HTTP payload
		String username = (String)payload.get("username");
		UUID uuid = UUID.fromString((String)payload.get("uuid"));
		String sessionToken = (String)payload.get("sessionToken");
		
		QRRedeemResult result = null;
		//Retrieve user account by username
		UserAccount account = userAccountRepository.findByUsername(username);
		//Verify account session token
		if(account.isValidSessionToken(sessionToken))
		{
			
			//Look up QR code by UUID in database
			QRCodeEntity qrCode = qrCodeRepository.findByuuid(uuid);
			//If QR Code exists, then redeem
			if(qrCode != null)
			{
				result = new QRRedeemResult();
				//Handle QR code depending on QR code type
				switch (qrCode.type)
				{
					//Item reward
					case 0:
						//Grant player account the item reward
						result.type = 0;
						result.message = "Item redeemed";
						result.encounterID = null;
					break;
					//Encounter
					case 1:
						try
						{
							//Create encounter
							Encounter tempEncounter = new Encounter(account, qrCode.encounterDefinitionPath);
							RpgServerApplication.encounterMap.put(tempEncounter.uuid, tempEncounter);
							
							//Create return value
							result.type = 1;
							result.message = "Created encounter with UUID " + tempEncounter.uuid.toString();
							result.encounterID = tempEncounter.uuid;
						}
						catch(IOException e)
						{
							e.printStackTrace();
						}
					default:
					
					break;
				}
			}
		}
		return result;
	}
	
	public class QRRedeemResult
	{
		//0 for item reward, 1 for encounter start
		int type;
		String message;
		//Used by client to reference the created encounter later in another API call
		UUID encounterID;
	}
}
