package com.management.finito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class FinitoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinitoApplication.class, args);
	}

}
