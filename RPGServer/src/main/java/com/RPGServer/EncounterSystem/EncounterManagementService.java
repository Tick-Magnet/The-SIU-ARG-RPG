package com.RPGServer.EncounterSystem;

import com.RPGServer.RpgServerApplication;
import com.RPGServer.Security.ClientIP;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Iterator;
import java.util.UUID;

@Service
public class EncounterManagementService
{
    //Five minutes in milliseconds
    private static final int ENCOUNTER_CLEANUP_RATE = 60000 * 1;

    //Scheduled method that goes through encounters int the hashmap and removes ones that should be deleted
    @Scheduled(fixedRate = ENCOUNTER_CLEANUP_RATE, initialDelay = ENCOUNTER_CLEANUP_RATE)
    private void cleanUpEncounters()
    {
        Iterator<UUID> iterator = RpgServerApplication.encounterMap.keySet().iterator();
        while(iterator.hasNext())
        {
            System.out.println("Cleaning up ");
            //Get Encounter object using key inside iterator
            Encounter currentEncounter = RpgServerApplication.encounterMap.get(iterator.next());
            if(currentEncounter.timeout.isBefore(LocalDateTime.now(ZoneId.of("UTC"))) || (currentEncounter.encounterComplete && currentEncounter.expiration.isBefore(LocalDateTime.now(ZoneId.of("UTC")))))
            {
                System.out.println("Encounter removed with UUID: " + currentEncounter.uuid.toString());
                RpgServerApplication.encounterMap.remove(currentEncounter.uuid);

            }
        }
    }
}
