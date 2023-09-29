package com.RPGServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

@SpringBootApplication
public class RpgServerApplication {

	@Autowired 
	private JavaMailSender mailSender; 
	
	@Autowired 
	private QRCodeRepository qrCodeRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(RpgServerApplication.class, args);
	}

}
