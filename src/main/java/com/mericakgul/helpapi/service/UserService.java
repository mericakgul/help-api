package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.core.helper.DtoMapper;
import com.mericakgul.helpapi.model.dto.AddressDto;
import com.mericakgul.helpapi.model.dto.UserRequest;
import com.mericakgul.helpapi.model.dto.UserResponse;
import com.mericakgul.helpapi.model.entity.Address;
import com.mericakgul.helpapi.model.entity.User;
import com.mericakgul.helpapi.repository.AddressRepository;
import com.mericakgul.helpapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final DtoMapper dtoMapper;

    @Transactional
    public UserResponse save(UserRequest userRequest) {
        User user = this.dtoMapper.mapModel(userRequest, User.class);
        User savedUser = this.userRepository.save(user);
        return this.dtoMapper.mapModel(savedUser, UserResponse.class);
    }
}
