package com.example.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

    @GetMapping("/timeout")
    public ResponseEntity timeout() throws InterruptedException {
        Thread.sleep(1500);
        return ResponseEntity.ok("timeout");
    }

    @GetMapping("/fallback")
    public ResponseEntity fallback() {
        return ResponseEntity.ok("fallback");
    }

}
