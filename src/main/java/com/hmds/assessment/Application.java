package com.hmds.assessment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application to run Spring Boot web framework for HMDS message API.
 */
@SpringBootApplication
public class Application {
    /**
     * Run the Sprint Boot web application to run the HMDS message API.
     * 
     * @param args Command line arguments with which to run Spring Boot
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
