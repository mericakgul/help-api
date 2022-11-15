package com.mericakgul.helpapi.controller;

import com.mericakgul.helpapi.model.dto.UserRequest;
import com.mericakgul.helpapi.model.dto.UserResponse;
import com.mericakgul.helpapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserResponse> findAllUsers() {
        return this.userService.findAll();
    }

    @GetMapping("/{username}")
    public UserResponse findByUsername(@PathVariable String username) {
        return this.userService.findByUsername(username);
    }

    @PutMapping("/{username}")
    public UserResponse update(@RequestBody UserRequest userRequest, @PathVariable String username ) {
        return this.userService.update(username, userRequest);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteByUsername(@PathVariable String username) {
        this.userService.deleteByUsername(username);
        return ResponseEntity.ok().body("The user " + username + " has been deleted along with its relations.");
    }
    /* When we do soft deleting we do not delete the user for real but just not returning it back to the client by filtering anymore.
     In this case we can still not create another user with the same username and email
     because uniqueness is still running for JPA and the user is still in the database.
     What kinda solution can be applied here?*/
}
