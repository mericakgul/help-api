package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.core.helper.DtoMapper;
import com.mericakgul.helpapi.model.dto.UserResponse;
import com.mericakgul.helpapi.model.entity.User;
import com.mericakgul.helpapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    public final UserRepository userRepository;
    public final DtoMapper dtoMapper;
    public List<UserResponse> findAll() {
        List<User> userList = this.userRepository.findAll();
        if(userList.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is not any user yet.");
        } else {
            return this.dtoMapper.mapListModel(userList, UserResponse.class);
        }
    }
}
