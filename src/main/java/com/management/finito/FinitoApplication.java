package com.management.finito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FinitoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinitoApplication.class, args);
	}

}
