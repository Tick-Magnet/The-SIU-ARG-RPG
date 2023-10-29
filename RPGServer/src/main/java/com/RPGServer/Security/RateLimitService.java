package com.RPGServer.Security;

import ch.qos.logback.core.net.server.Client;
import com.RPGServer.RpgServerApplication;
import jakarta.persistence.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.LoadingCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class RateLimitService
{
    public enum CallType
    {
        NORMAL,
        LOGIN,
        BAD_LOGIN
    }
    //RESET COUNTER VALUES
    //Reset counter for failed logins each hour
    private static final int BAD_PASSWORD_RESET_RATE = 3600000;

    //Reset counter for general API request limit each minute
    private static final int API_LIMIT_RESET_RATE = 60000;

    //LIMIT VALUES
    //Amount of bad password attempts allowed per reset cycle
    private static final int BAD_PASSWORD_LIMIT = 40;
    //Amount of API calls allowed per reset cycle
    private static final int GENERAL_API_LIMIT = 80;


    //Thread pool for filter threads
    private static ExecutorService threadPool = Executors.newCachedThreadPool();
    public void badLogin(String address)
    {
        filterIP(address, CallType.BAD_LOGIN);
    }
    //Check IP to see if it has reached limits before allowing the request
    public boolean filterIP(String address, CallType type) {
        boolean output = true;

        //Add thread to thread pool
        Future<Boolean> task = threadPool.submit(new FilterThread(address, type));
        //Block for thread completion
        try {
            output = task.get();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return output;
    }

    private class FilterThread implements Callable<Boolean>
    {
        boolean allow = true;
        String address;
        CallType callType;
        public FilterThread(String address, CallType callType)
        {
            this.address = address;
            this.callType = callType;
        }

        public void run()
        {
            System.out.println("Checking");
            //Attempt to lookup address on the hashmap
            ClientIP clientIP = RpgServerApplication.clientIPMap.get(address);
            //If it does not yet exist, create one
            if(clientIP == null)
            {
                clientIP = new ClientIP();
                clientIP.IP = address;
                RpgServerApplication.clientIPMap.put(clientIP.IP, clientIP);
                System.out.println("New client @ " + address);
            }
            //Check if the passed IP address has exceeded its API limits
            switch (callType)
            {
                case LOGIN:
                    if(clientIP.failedLogins >= BAD_PASSWORD_LIMIT)
                    {
                        //Block api call
                        allow = false;
                        System.out.println("Blocking " + clientIP.IP + " for too many password attempts");
                    }
                    break;
                case NORMAL:
                    clientIP.APICalls++;
                    if(clientIP.APICalls >= GENERAL_API_LIMIT)
                    {
                        allow = false;
                    }
                    break;
                case BAD_LOGIN:
                    clientIP.failedLogins++;
                default:
                    break;
            }
        }
        @Override
        public Boolean call()
        {
            run();
            return allow;
        }
    }
    //Reset counters in cached ip addresses for bad login attempts
    @Scheduled(fixedRate = BAD_PASSWORD_RESET_RATE, initialDelay = BAD_PASSWORD_RESET_RATE)
    private void clearBadPasswordLimit()
    {
        Iterator<String> iterator = RpgServerApplication.clientIPMap.keySet().iterator();
        while(iterator.hasNext())
        {
            //Get ClientIP object using key inside iterator
            ClientIP tempIP = RpgServerApplication.clientIPMap.get(iterator.next());
            tempIP.failedLogins = 0;
        }
    }
    //Reset counters in cached ip addresses for general API calls
    @Scheduled(fixedRate = API_LIMIT_RESET_RATE, initialDelay = API_LIMIT_RESET_RATE)
    private void clearGeneralAPILimit()
    {
        Iterator<String> iterator = RpgServerApplication.clientIPMap.keySet().iterator();
        while(iterator.hasNext())
        {
            //Get ClientIP object using key inside iterator
            ClientIP tempIP = RpgServerApplication.clientIPMap.get(iterator.next());
            tempIP.APICalls = 0;
        }
    }
}
