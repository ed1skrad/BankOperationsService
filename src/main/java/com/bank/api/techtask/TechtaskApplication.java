package com.bank.api.techtask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TechtaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechtaskApplication.class, args);
	}

}
