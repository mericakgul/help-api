package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.auth.TokenManager;
import com.mericakgul.helpapi.model.dto.LoginDto;
import com.mericakgul.helpapi.model.dto.UserRequest;
import com.mericakgul.helpapi.model.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenManager tokenManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MyUserDetailsService myUserDetailsService;

    public HttpHeaders login(LoginDto loginDto) {
        if(loginDto != null) {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + this.tokenManager.generateToken(loginDto.getUsername()));
            return headers;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body not found.");
        }
    }
    @Transactional
    public UserResponse signUp(UserRequest userRequest){
        try{
            userRequest.setPassword((this.bCryptPasswordEncoder.encode(userRequest.getPassword())));
            return this.myUserDetailsService.save(userRequest);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
