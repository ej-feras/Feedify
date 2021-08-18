package com.feedify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class FeedifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedifyApplication.class, args);

	}

}
