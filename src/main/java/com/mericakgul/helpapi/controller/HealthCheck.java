package com.mericakgul.helpapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthcheck")
public class HealthCheck {

    @GetMapping
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok("OK");
    }
}
