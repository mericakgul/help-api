package com.mericakgul.helpapi.core.helper;

import com.mericakgul.helpapi.model.dto.UserRequest;
import com.mericakgul.helpapi.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ObjectUpdaterHelper {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void updateUserObject(User currentUser, UserRequest userRequest){
        currentUser.setPassword(this.bCryptPasswordEncoder.encode(userRequest.getPassword()));
        currentUser.setFullName(userRequest.getFullName());
        currentUser.setPhoneNumber(userRequest.getPhoneNumber());
        currentUser.setDescription(userRequest.getDescription());
    }

}