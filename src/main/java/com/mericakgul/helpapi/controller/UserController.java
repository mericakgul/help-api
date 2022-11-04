package com.mericakgul.helpapi.controller;

import com.mericakgul.helpapi.model.dto.UserResponse;
import com.mericakgul.helpapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    // find user by id

    // delete user by id

    // update user details

    // find users by filter

}
