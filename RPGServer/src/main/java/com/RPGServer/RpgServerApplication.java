package com.RPGServer;

import com.RPGServer.EncounterSystem.Encounter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;

import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

import org.springframework.web.servlet.config.annotation.*;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class RpgServerApplication {

	@Autowired 
	private JavaMailSender mailSender; 
	
	@Autowired 
	private QRCodeRepository qrCodeRepository;
	
	//Hashmap for storing encounters 
	public static ConcurrentHashMap<UUID, Encounter> encounterMap = new ConcurrentHashMap<UUID, Encounter>();
		
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
