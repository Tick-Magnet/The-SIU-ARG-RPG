package com.RPGServer.RESTControllers;

import com.RPGServer.ItemSystem.Item;
import com.RPGServer.QRCodeEntity;
import com.RPGServer.QRCodeRepository;
import com.RPGServer.UserAccount;
import com.RPGServer.UserAccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class InventoryController
{

    @Autowired
    private UserAccountRepository userAccountRepository;
    //Most item access is done through its index, returns an array of items along with their name
    @PostMapping("/getInventory")
    public Map<String,Object> getInventory(@RequestBody Map<String, Object> payload)
    {
        HashMap<String, Object> output = new HashMap<String, Object>();
        //Obtain fields from payload
        String username = (String) payload.get("username");
        String token = (String) payload.get("token");

        //Retrieve user account
        UserAccount user = userAccountRepository.findByUsername(username);

        //Authenticate
        if(authenticate(user, token))
        {
            output.put("inventory", user.playerCharacter.getInventoryItems().toArray());
        }
        else
        {
            output.put("message", "Failed to authenticate");
        }

        return output;
    }
    //Returns the entire item object from a passed index
    @PostMapping("/inspectItemSlot")
    public Map<String,Object> inspectItemSlot(@RequestBody Map<String, Object> payload)
    {
        HashMap<String, Object> output = new HashMap<String, Object>();
        //Obtain fields from payload
        String username = (String) payload.get("username");
        String token = (String) payload.get("token");
        int index = (int)payload.get("index");
        //Retrieve user account
        UserAccount user = userAccountRepository.findByUsername(username);

        //Authenticate
        if(authenticate(user, token))
        {
            if(index >= 0 && index < user.playerCharacter.getInventoryItems().size()) {
                output.put("item", user.playerCharacter.getItem(index));
            }
            else
            {
                output.put("message", "No item located at " + index);
            }
        }
        else
        {
            output.put("message", "Failed to authenticate");
        }
        return output;
    }
    //Equip an item into a certain equipment slot. Item should be passed by index
    @PostMapping("/equipItem")
    public Map<String,Object> equipItem(@RequestBody Map<String, Object> payload)
    {
        HashMap<String, Object> output = new HashMap<String, Object>();
        //Obtain fields from payload
        String username = (String) payload.get("username");
        String token = (String) payload.get("token");

        int index = (int) payload.get("index");
        Item.ItemType type = Item.ItemType.valueOf((String)payload.get("itemType"));

        //Retrieve user account
        UserAccount user = userAccountRepository.findByUsername(username);

        //Authenticate
        if(authenticate(user, token))
        {
            boolean equipped = false;
            if(index >= 0 && index < user.playerCharacter.getInventoryItems().size())
            {
                Item item = user.playerCharacter.getItem(index);
                switch (type)
                {
                    case HELMET:
                        equipped = user.playerCharacter.equipItem(1, item);
                        break;
                    case CHEST_ARMOR:
                        equipped = user.playerCharacter.equipItem(2, item);
                        break;
                    case LEG_ARMOR:
                        equipped = user.playerCharacter.equipItem(3, item);
                        break;
                    case BOOTS:
                        equipped = user.playerCharacter.equipItem(4, item);
                        break;
                    case WEAPON:
                        equipped = user.playerCharacter.equipItem(5, item);
                        break;
                    default:
                        output.put("message", "Not a valid item type");
                        break;
                }
                output.put("itemEquipped", equipped);
                userAccountRepository.save(user);
            }
            else
            {
                output.put("message", "Invalid item index");
            }
        }
        else
        {
            output.put("message", "Failed to authenticate");
        }

        return output;
    }
    //Remove item from player inventory and add the gold value to the players gold
    @PostMapping("/sellItem")
    public Map<String,Object> sellItem(@RequestBody Map<String, Object> payload)
    {
        HashMap<String, Object> output = new HashMap<String, Object>();
        //Obtain fields from payload
        String username = (String) payload.get("username");
        String token = (String) payload.get("token");
        int index = (int) payload.get("index");

        UserAccount user = userAccountRepository.findByUsername(username);

        if(authenticate(user, token))
        {
            if(index >= 0 && index < user.playerCharacter.getInventoryItems().size())
            {
                Item item = user.playerCharacter.removeItem(index);
                user.playerCharacter.gold += item.goldValue;
                output.put("message", "Item sold for" + item.goldValue);
            }
            else
            {
                output.put("message", "invalid inventory slot");
            }
            userAccountRepository.save(user);

        }
        else
        {
            output.put("message", "Failed to authenticate");
        }
        return output;
    }

    private boolean authenticate(UserAccount user, String token)
    {
        return user.isValidSessionToken(token);
    }
}
