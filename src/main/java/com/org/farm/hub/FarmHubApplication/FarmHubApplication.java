package com.org.farm.hub.FarmHubApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FarmHubApplication {

	public static void main(String[] args) {
		System.setProperty("com.mysql.cj.disableAbandonedConnectionCleanup", "true");
		SpringApplication.run(FarmHubApplication.class, args);
	}

}
