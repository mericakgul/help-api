package com.mericakgul.helpapi.controller;

import com.mericakgul.helpapi.model.dto.UserResponse;
import com.mericakgul.helpapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

     public final UserService userService;

     // findallusers
    @GetMapping
    public List<UserResponse> findAllUsers() {
        return this.userService.findAll();
    }

    @GetMapping("/{userId}")
    public UserResponse findByUuid(@PathVariable UUID userId){
        return this.userService.findByUuid(userId);
    }
    // find user by id

    // delete user by id

    // update user details

    // find users by filter

}
