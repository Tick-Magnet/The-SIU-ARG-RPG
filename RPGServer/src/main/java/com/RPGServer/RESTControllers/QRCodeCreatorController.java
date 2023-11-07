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
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.awt.Color;
import java.awt.Graphics2D;

@RestController
public class QRCodeCreatorController
{
    private final static String TYPE_0_BASE_PATH_GREEN = "/images/treasureGreen.png";
    private final static String TYPE_0_BASE_PATH_YELLOW = "/images/treasureYellow.png";
    private final static String TYPE_0_BASE_PATH_RED = "/images/treasureRed.png";
    private final static String TYPE_1_BASE_PATH_GREEN = "/images/battleGreen.png";
    private final static String TYPE_1_BASE_PATH_YELLOW = "/images/battleYellow.png";
    private final static String TYPE_1_BASE_PATH_RED = "/images/battleRed.png";


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
        int colorType = (int)payload.get("colorType");
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

                        output.put("image",generateQRImage(DECODE_URL, tempQR.uuid.toString(), 0, colorType));
                        break;
                        //Encounter Case
                    case 1:
                        String encounterDefinitionPath = (String) payload.get("encounterDefinitionPath");
                        tempQR = new QRCodeEntity();
                        tempQR.type=1;
                        tempQR.encounterDefinitionPath = encounterDefinitionPath;
                        qrCodeRepository.save(tempQR);

                        //Create image
                        output.put("image",generateQRImage(DECODE_URL, tempQR.uuid.toString(),1, colorType));

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

    private String generateQRImage(String url, String uuid, int type, int colorType) throws Exception
    {
        int xOffset, yOffset;
        String output;
        BufferedImage qrImage;
        BufferedImage baseImage;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if(type == 0)
        {
            if(colorType == 0)
            {
                baseImage = ImageIO.read(loader.getResource(TYPE_0_BASE_PATH_GREEN));
            }
            else if(colorType == 1)
            {
                baseImage = ImageIO.read(loader.getResource(TYPE_0_BASE_PATH_YELLOW));
            }
            else
            {
                baseImage = ImageIO.read(loader.getResource(TYPE_0_BASE_PATH_RED));
            }
		}
		else
		{
            if(colorType == 0)
            {
                baseImage = ImageIO.read(loader.getResource(TYPE_1_BASE_PATH_GREEN));
            }
            else if(colorType == 1)
            {
                baseImage = ImageIO.read(loader.getResource(TYPE_1_BASE_PATH_YELLOW));
            }
            else
            {
                baseImage = ImageIO.read(loader.getResource(TYPE_1_BASE_PATH_RED));
            }
		}
        System.out.println(baseImage.getHeight());
        //Generate QR code image from UUID
        String fullText = url + uuid;
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(fullText, BarcodeFormat.QR_CODE, 300, 300);
        qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        xOffset = baseImage.getWidth() / 2 - qrImage.getWidth()/2;
        yOffset = 375;

                //Combine QR code image with base image
		Graphics2D graphicsContext = baseImage.createGraphics();
		//Draw base image to final image
		
		//Draw QR code image to final image
		graphicsContext.drawImage(qrImage, xOffset, yOffset, null);
        //Encode in base64
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(baseImage, "jpg", stream);
        byte[] imageBytes = stream.toByteArray();

        output = Base64.getEncoder().encodeToString(imageBytes);

        //graphicsContext.dispose();
        return output;
    }
}
