package com.hostfully.webservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebserviceApplication {

	private final static Logger log = LoggerFactory.getLogger(WebserviceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(WebserviceApplication.class, args);
		log.info("Running webservice application...");
	}

}
