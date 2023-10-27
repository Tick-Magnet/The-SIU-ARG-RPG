package com.RPGServer.Security;

import jakarta.persistence.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.LoadingCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService
{
    //Reset counter for failed logins each hour
    private static final int BAD_PASSWORD_RESET_RATE = 3600000;

    @Autowired
    private ClientIPRepository clientIPRepository;

    //Reset counters in cached ip addresses for bad login attemps
    @Scheduled(fixedRate = BAD_PASSWORD_RESET_RATE, initialDelay = BAD_PASSWORD_RESET_RATE)
    private void clearBadPasswordLimit()
    {

    }
}
