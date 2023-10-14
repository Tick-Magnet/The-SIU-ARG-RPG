package com.RPGServer.RESTControllers;

import com.RPGServer.QRCodeEntity;
import com.RPGServer.QRCodeRepository;
import com.RPGServer.UserAccount;
import com.RPGServer.UserAccountRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


public class QRCodeCreatorController
{
    private final static String DECODE_URL = "http://localhost/redeemQR?uuid=";
    @Autowired
    private QRCodeRepository qrCodeRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @PostMapping("/createQRCode")
    public Map<String,Object> createQR(@RequestBody Map<String, Object> payload)throws Exception
    {
        HashMap<String, Object> output = new HashMap<String, Object>();
        //Obtain fields from payload
        String username = (String)payload.get("username");
        String token = (String)payload.get("token");
        int type = (int)payload.get("type");
        //Verify user session token
        UserAccount user = userAccountRepository.findByUsername(username);
        if(user != null && user.isValidSessionToken(token))
        {
            //Check users permission level
            if(user.userRole == user.userRole.ADMIN || user.userRole == user.userRole.GAME_MASTER)
            {
                QRCodeEntity tempQR;

                //Generate QR code in database
                switch(type)
                {
                    //Item Case
                    case 0:
                        String itemDefinitionPath = (String) payload.get("itemDefinitionPath");
                        int goldReward = (int) payload.get("goldReward");
                        int experienceReward = (int) payload.get("experienceReward");
                        tempQR = new QRCodeEntity();
                        tempQR.itemDefinitionPath = itemDefinitionPath;
                        tempQR.type = 0;
                        tempQR.goldReward = goldReward;

                        tempQR.experienceReward = experienceReward;
                        qrCodeRepository.save(tempQR);

                        //Create image

                        output.put("image",generateQRImage(DECODE_URL, tempQR.uuid.toString()));
                        break;
                        //Encounter Case
                    case 1:
                        String encounterDefinitionPath = (String) payload.get("encounterDefinitionPath");
                        tempQR = new QRCodeEntity();

                        tempQR.encounterDefinitionPath = encounterDefinitionPath;
                        qrCodeRepository.save(tempQR);

                        //Create image
                        output.put("image",generateQRImage(DECODE_URL, tempQR.uuid.toString()));

                        break;
                    default:
                        output.put("message","Qr code type invalid");
                        break;
                }
            }
            else
            {
                output.put("message", "Unauthorized");
            }
        }
        else
        {
            output.put("message", "Could not validate login session");
        }
        return output;
    }

    private String generateQRImage(String url, String uuid) throws Exception
    {
        String output;

        BufferedImage image;
        String fullText = url + uuid;
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(fullText, BarcodeFormat.QR_CODE, 200, 200);
        image = MatrixToImageWriter.toBufferedImage(bitMatrix);

        //Encode in base64
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", stream);
        byte[] imageBytes = stream.toByteArray();

        output = Base64.getEncoder().encodeToString(imageBytes);

        return output;
    }
}
