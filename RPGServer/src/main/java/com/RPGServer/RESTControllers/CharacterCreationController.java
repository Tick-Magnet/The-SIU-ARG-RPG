package com.RPGServer.RESTControllers;

import com.RPGServer.PlayerCharacter;
import com.RPGServer.CharacterType;


import com.RPGServer.UserAccount;
import com.RPGServer.UserAccountRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;


@RestController
public class CharacterCreationController
{
    @Autowired
    private UserAccountRepository userAccountRepository;

    @PostMapping("/createCharacter")
    public Map<String,Object> createCharacter(@RequestBody Map<String, Object> payload)
    {
        PlayerCharacter outputCharacter = null;
        HashMap<String,Object> output = new HashMap<String, Object>();
        String username = (String)payload.get("username");
        String sessionToken = (String)payload.get("token");
        //Retrieve user account
        UserAccount player = userAccountRepository.findByUsername(username);
        //Verify session token
        if(player != null && player.isValidSessionToken(sessionToken))
        {
            System.out.println("Verified");
            //Case where character has not yet been created
            //JSON from client should only include username and session token
            if (player.playerCharacter == null || !player.playerCharacter.statsRolled)
            {
                System.out.println("Rolling stats");
                player.playerCharacter.rollStats();
                //Create a default character type (for jakarta null column restriction)
                player.playerCharacter.characterType = new CharacterType();
                userAccountRepository.save(player);
                outputCharacter = player.playerCharacter;
                output.put("character", outputCharacter);
            }
            //Case where stats have been rolled, client should be passing desired class and race fields
            else if (player.playerCharacter.statsRolled && player.playerCharacter.creationComplete != true)
            {
                CharacterType.CharacterClass characterClass = null;
                CharacterType.CharacterRace characterRace = null;
                System.out.println("Part 2");
                try
                {
                    characterClass = CharacterType.CharacterClass.valueOf(((String) payload.get("characterClass")).toUpperCase());
                    characterRace = CharacterType.CharacterRace.valueOf(((String) payload.get("characterRace")).toUpperCase());
                } catch (IllegalArgumentException exception) {

                }
                if (characterClass != null && characterRace != null) {
                    //Create CharacterType
                    CharacterType characterType = new CharacterType(characterClass, characterRace);
                    player.playerCharacter.creationComplete = true;
                    //Set player characterType
                    player.playerCharacter.characterType = characterType;
                    userAccountRepository.save(player);

                    outputCharacter = player.playerCharacter;
                    output.put("character", outputCharacter);
                }

            }
        }
        return output;
    }
}
