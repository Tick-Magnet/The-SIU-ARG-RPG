package com.RPGServer.RESTControllers;


import com.RPGServer.EncounterSystem.*;
import com.RPGServer.RpgServerApplication;
import com.RPGServer.UserAccount;
import com.RPGServer.UserAccountRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
public class EncounterController
{
    @Autowired
    private UserAccountRepository userAccountRepository;

    @PostMapping("/postEncounterUpdate")
    public Map<String,Object> postEncounterUpdate(@RequestBody Map<String, Object> payload)
    {
        HashMap<String,Object> output = new HashMap<String,Object>();
        //Retrieve player account by username
        UserAccount player = userAccountRepository.findByUsername((String)payload.get("username"));
        if(player != null)
        {
            //Verify session token
            if(player.isValidSessionToken((String)payload.get("token")) == true)
            {
                //Attempt to retrieve encounter
                UUID encounterID = UUID.fromString((String)payload.get("encounterID"));
                Encounter encounter = RpgServerApplication.encounterMap.get(encounterID);

                if(encounter != null)
                {
                    StepUpdate input = new StepUpdate(payload);
                    //Post update and return result
                    output = (HashMap<String, Object>)encounter.postStepUpdate(input).toMap();
                }
            }
        }
        return output;
    }

}
