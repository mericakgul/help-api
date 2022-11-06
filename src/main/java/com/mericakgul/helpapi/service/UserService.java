package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.core.helper.DtoMapper;
import com.mericakgul.helpapi.core.helper.ObjectUpdaterHelper;
import com.mericakgul.helpapi.model.dto.UserRequest;
import com.mericakgul.helpapi.model.dto.UserResponse;
import com.mericakgul.helpapi.model.entity.Address;
import com.mericakgul.helpapi.model.entity.User;
import com.mericakgul.helpapi.repository.AddressRepository;
import com.mericakgul.helpapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    private final AddressService addressService;
    private final DtoMapper dtoMapper;
    private final ObjectUpdaterHelper objectUpdaterHelper;
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
    public UserResponse findByUsername(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user found with this username."));
        return this.dtoMapper.mapModel(user, UserResponse.class);
    }

    @Transactional
    public void deleteByUsername(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user found with this username to delete."));
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if(Objects.equals(username, loggedInUsername)){
            this.addressService.deleteRelatedAddresses(user.getUserUuid());
            user.setDeletedDate(new Date());
            this.userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorised to delete the user: " + username);
            // UNAUTHORIZED exception doesn't show the message, how can I get it shown?
        }
    }

    // TODO complete update method
//    @Transactional
//    public UserResponse update(UUID userUuid, UserRequest userRequest) {
//        Optional<User> optionalUser = this.userRepository.findByUserUuid(userUuid);
//        if (optionalUser.isPresent()){
//            User currentUser = optionalUser.get();
//            this.objectUpdaterHelper.updateUserObject(currentUser, userRequest);
//            return this.dtoMapper.mapModel(currentUser, UserResponse.class);
//        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user found with this id to update.");
//        }
//    }
}
