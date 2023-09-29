package com.RPGServer;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import org.springframework.web.servlet.ModelAndView;


@RestController
public class QRCodeRedeemController
{
	@Autowired
	private UserAccountRepository userAccountRepository;
	
	@Autowired
	private QRCodeRepository qrCodeRepository;
	
	//Using UUID for QR code token, not as secure but easier to include in a URL
	@PostMapping("/redeemQR")
	public QRRedeemResult redeem(@RequestParam(value = "username", required = "true") String username, @RequestParam(value = "sessionToken", required = "true") byte[] sessionToken, @RequestParam(value = "uuid", required= "true") UUID uuid)
	{
		QRRedeemResult result = null;
		//Retrieve user account by username
		UserAccount account = userAccountRepository.findByUsername(username);
		//Verify account session token
		if(account.isValidSessionToken(sessionToken))
		{
			//Look up QR code by UUID in database
			QRCodeEntity qrCode = qrCodeRepository.findByUUID(uuid);
			
		}
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
