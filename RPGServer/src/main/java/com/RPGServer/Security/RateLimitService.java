package com.RPGServer.Security;

import ch.qos.logback.core.net.server.Client;
import com.RPGServer.RpgServerApplication;
import jakarta.persistence.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.LoadingCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RateLimitService
{
    //RESET COUNTER VALUES
    //Reset counter for failed logins each hour
    private static final int BAD_PASSWORD_RESET_RATE = 3600000;

    //Reset counter for general API request limit each minute
    private static final int API_LIMIT_RESET_RATE = 60000;

    //LIMIT VALUES
    //Amount of bad password attempts allowed per reset cycle
    private static final int BAD_PASSWORD_LIMIT = 100;
    //Amount of API calls allowed per reset cycle
    private static final int GENERAL_API_LIMIT = 80;
    @Autowired
    private ClientIPRepository clientIPRepository;

    //Reset counters in cached ip addresses for bad login attempts
    @Scheduled(fixedRate = BAD_PASSWORD_RESET_RATE, initialDelay = BAD_PASSWORD_RESET_RATE)
    private void clearBadPasswordLimit()
    {
        ClientIP[] clients = RpgServerApplication.clientIPMap.values();
    }
    //Reset counters in cached ip addresses for general API calls
    @Scheduled(fixedRate = API_LIMIT_RESET_RATE, initialDelay = API_LIMIT_RESET_RATE)
    private void clearGeneralAPILimit()
    {

    }
}
