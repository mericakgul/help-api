package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.core.helper.DtoMapper;
import com.mericakgul.helpapi.model.dto.UserResponse;
import com.mericakgul.helpapi.model.entity.User;
import com.mericakgul.helpapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    public final UserRepository userRepository;
    public final DtoMapper dtoMapper;
    public List<UserResponse> findAll() {
        List<User> userList = this.userRepository.findAll();
        return this.dtoMapper.mapListModel(userList, UserResponse.class);
        // Note: To be able to make a request to this end point a token is needed. If there is a valid token,
        // which means there is at least one user in the db for sure. Therefore, the check below is removed.
//        if(userList.isEmpty()){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is not any user yet.");
//        } else {
//            return this.dtoMapper.mapListModel(userList, UserResponse.class);
//        }
    }
    public UserResponse findByUuid(UUID userId) {
        User user = this.userRepository.findByUuid(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user found with this id."));
        return this.dtoMapper.mapModel(user, UserResponse.class);
    }

    @Transactional
    public void deleteByUuid(UUID userId) {
        User user = this.userRepository.findByUuid(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user found with this id to delete."));
        user.setDeletedDate(new Date());
        this.userRepository.save(user);
    }
}
