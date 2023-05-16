package com.example.firebase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example")
public class FirebaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirebaseApplication.class, args);
	}

}
