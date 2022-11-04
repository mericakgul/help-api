package com.mericakgul.helpapi.controller;

import com.mericakgul.helpapi.model.dto.UserResponse;
import com.mericakgul.helpapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteByUuid(@PathVariable UUID userId){
        this.userService.deleteByUuid(userId);
        return ResponseEntity.ok().body("The user with this uuid has been deleted: " + userId);
    }
    // When we do soft deleting we do not delete the user for real but just not showing it anymore.
    // In this case we can still not create another user with the same username and email because uniqueness is still running for JPA.
    // What kinda solution can be applied here?



    // update user details

    // find users by filter

}
