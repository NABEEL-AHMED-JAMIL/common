package com.barco.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Nabeel Ahmed
 */
@SpringBootApplication
public class CommonApplication {

	public Logger logger = LogManager.getLogger(CommonApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CommonApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return (args) -> {
			logger.info("Common Application Started Successfully");
		};
	}

}