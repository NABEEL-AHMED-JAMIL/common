package com.barco.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


@SpringBootApplication
public class CommonApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommonApplication.class, args);
	}

	@PostConstruct
	public void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
	}

}
