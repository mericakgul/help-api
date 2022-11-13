package com.mericakgul.helpapi.core.helper;

import com.mericakgul.helpapi.model.entity.User;
import com.mericakgul.helpapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class UserExistence {
    private final UserRepository userRepository;

    public User checkIfUserExistsAndReturn(String username){
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user found with this username."));
    }
}
