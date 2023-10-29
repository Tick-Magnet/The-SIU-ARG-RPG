package com.RPGServer.Security;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ClientIP
{
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer Id;

    public ClientIP()
    {

    }

    public String IP;

    public int failedLogins;

    public int APICalls;

}
