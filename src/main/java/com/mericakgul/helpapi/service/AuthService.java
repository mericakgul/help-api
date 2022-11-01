package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.auth.TokenManager;
import com.mericakgul.helpapi.core.helper.DtoMapper;
import com.mericakgul.helpapi.model.dto.LoginDto;
import com.mericakgul.helpapi.model.dto.UserRequest;
import com.mericakgul.helpapi.model.dto.UserResponse;
import com.mericakgul.helpapi.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenManager tokenManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DtoMapper dtoMapper;
    private final UserDetailService userDetailService;

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
    public UserResponse signUp(UserRequest userRequest){
        try{
            User user = this.dtoMapper.mapModel(userRequest, User.class);
            user.setPassword((this.bCryptPasswordEncoder.encode(userRequest.getPassword())));
            return this.userDetailService.save(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}