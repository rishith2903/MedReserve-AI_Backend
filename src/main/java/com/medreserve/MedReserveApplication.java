package com.medreserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MedReserveApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedReserveApplication.class, args);
    }

}
