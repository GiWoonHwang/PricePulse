package com.example.dustin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PricePulseApplication {

    public static void main(String[] args) {
        SpringApplication.run(PricePulseApplication.class, args);
    }

}