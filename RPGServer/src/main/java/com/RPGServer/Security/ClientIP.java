package com.RPGServer.Security;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity
public class ClientIP
{
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer Id;

    public ClientIP()
    {
        failedLogins = 0;
        APICalls = 0;
        registerCalls = 0;
        lastSeen = Instant.now();
    }

    public String IP;

    public int failedLogins;

    public int APICalls;
    public int registerCalls;

    public Instant lastSeen;

}
