package com.waggle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WaggleApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaggleApplication.class, args);
    }

}
