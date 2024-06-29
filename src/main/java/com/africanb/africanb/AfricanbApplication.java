package com.africanb.africanb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class AfricanbApplication {
	public static void main(String[] args) {
		SpringApplication.run(AfricanbApplication.class, args);
	}

}
