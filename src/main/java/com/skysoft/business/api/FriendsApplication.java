package com.skysoft.business.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class FriendsApplication {
    public static void main(String[] args) {
        SpringApplication.run(FriendsApplication.class, args);
    }

}
