package com.moyeorait.moyeoraitspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class MoyeoraItSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoyeoraItSpringApplication.class, args);
    }

}
