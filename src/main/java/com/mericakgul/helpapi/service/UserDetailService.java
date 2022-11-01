package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.core.helper.DtoMapper;
import com.mericakgul.helpapi.model.dto.UserResponse;
import com.mericakgul.helpapi.model.entity.User;
import com.mericakgul.helpapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    private final DtoMapper dtoMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no user found with this username."));
    }

    public UserResponse save(User user) {
        User savedUser = this.userRepository.save(user);
        return this.dtoMapper.mapModel(savedUser, UserResponse.class);
    }
}
