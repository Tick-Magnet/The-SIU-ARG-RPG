package com.RPGServer.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DiscordWebhookService
{

    final String url = "";
    public static RestTemplate restTemplate = new RestTemplate();

    public void outputDiscord(String message)
    {
        try {
            Request request = new Request();
            request.content = message;
            restTemplate.postForObject(url, request, Object.class);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private class Request
    {
        public String content;
    }
}
