package com.mericakgul.helpapi.controller;

import com.mericakgul.helpapi.model.dto.LoginDto;
import com.mericakgul.helpapi.model.dto.UserRequest;
import com.mericakgul.helpapi.model.dto.UserResponse;
import com.mericakgul.helpapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // Login is sent with Post request to be able to send the login info in requestbody.
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto loginDto){
        return ResponseEntity.ok().headers(this.authService.login(loginDto)).body("Login success");
    }

    @PostMapping("/signup")
    public UserResponse signUp(@Valid @RequestBody UserRequest userRequest){
        return this.authService.signUp(userRequest);
    }

}
