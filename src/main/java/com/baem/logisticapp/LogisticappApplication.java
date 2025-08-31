package com.baem.logisticapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LogisticappApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogisticappApplication.class, args);
	}

}
