package com.RPGServer.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService
{
    @Autowired
    private ClientIPRepository clientIPRepository;


}
