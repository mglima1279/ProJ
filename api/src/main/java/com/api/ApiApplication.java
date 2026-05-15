package com.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class ApiApplication {

    @Value("${server.port}")
    private String port;

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    @PostConstruct
    public void printPort() {
        System.out.println("");
        System.out.println("--------------------------------------------");
        System.out.println("");
        System.out.println("Server is running on port: " + port);
        System.out.println("");
        System.out.println("--------------------------------------------");
        System.out.println("");
    }
}
