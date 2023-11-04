package com.RPGServer;

import com.RPGServer.EncounterSystem.Encounter;
import com.RPGServer.Security.ClientIP;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;

import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableScheduling
public class RpgServerApplication {

	@Autowired 
	private JavaMailSender mailSender; 
	@Autowired
	private static UserAccountRepository userAccountRepository;
	@Autowired 
	private QRCodeRepository qrCodeRepository;
	
	//Hashmap for storing encounters 
	public static ConcurrentHashMap<UUID, Encounter> encounterMap = new ConcurrentHashMap<UUID, Encounter>();

	//Hashmap for storing IP addresses for clients
	public static ConcurrentHashMap<String, ClientIP> clientIPMap = new ConcurrentHashMap<String, ClientIP>();
	public static void main(String[] args) {
		SpringApplication.run(RpgServerApplication.class, args);
	}
	
	@Configuration
	@EnableWebMvc
	public class Config implements WebMvcConfigurer
	{
		//Allows cross origin resource sharing (CORS)
		@Override
		public void addCorsMappings(CorsRegistry registry)
		{
			registry.addMapping("/**");
		}
	}

}
