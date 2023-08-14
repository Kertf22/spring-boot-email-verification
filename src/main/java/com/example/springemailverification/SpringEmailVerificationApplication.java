package com.example.springemailverification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringEmailVerificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringEmailVerificationApplication.class, args);
	}

}
