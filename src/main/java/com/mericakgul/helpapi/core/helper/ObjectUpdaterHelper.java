package com.mericakgul.helpapi.core.helper;

import com.mericakgul.helpapi.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ObjectUpdaterHelper {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void updateUserObjectPrimitiveFields(User currentUser, User upToDateUser){
        currentUser.setUsername(upToDateUser.getUsername());
        currentUser.setPassword(this.bCryptPasswordEncoder.encode(upToDateUser.getPassword()));
        currentUser.setFullName(upToDateUser.getFullName());
        currentUser.setPhoneNumber(upToDateUser.getPhoneNumber());
        currentUser.setDescription(upToDateUser.getDescription());
        currentUser.setSkills(upToDateUser.getSkills());
    }
}