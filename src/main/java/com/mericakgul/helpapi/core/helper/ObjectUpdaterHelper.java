package com.mericakgul.helpapi.core.helper;

import com.mericakgul.helpapi.model.dto.AddressDto;
import com.mericakgul.helpapi.model.dto.AssignmentRequest;
import com.mericakgul.helpapi.model.entity.Address;
import com.mericakgul.helpapi.model.entity.Assignment;
import com.mericakgul.helpapi.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ObjectUpdaterHelper {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ObjectExistence objectExistence;

    public void updateUserObjectPrimitiveFields(User currentUser, User upToDateUser) {
        currentUser.setUsername(upToDateUser.getUsername());
        currentUser.setPassword(this.bCryptPasswordEncoder.encode(upToDateUser.getPassword()));
        currentUser.setFullName(upToDateUser.getFullName());
        currentUser.setPhoneNumber(upToDateUser.getPhoneNumber());
        currentUser.setDescription(upToDateUser.getDescription());
        currentUser.setSkills(upToDateUser.getSkills());
    }

    public void updateAddressObjectPrimitiveFields(Address currentAddress, AddressDto upToDateAddress) {
        currentAddress.setStreetName(upToDateAddress.getStreetName());
        currentAddress.setHouseNumber(upToDateAddress.getHouseNumber());
        currentAddress.setZipCode(upToDateAddress.getZipCode());
        currentAddress.setCity(upToDateAddress.getCity());
        currentAddress.setCountry(upToDateAddress.getCountry());
    }

    public void updateAssignmentObjectFields(Assignment currentAssignment, AssignmentRequest upToDateAssignment) {
        User newServiceProviderUser = this.objectExistence.checkIfUserExistsAndReturn(upToDateAssignment.getServiceProviderUsername());
        currentAssignment.setDescription(upToDateAssignment.getDescription());
        currentAssignment.setServiceProviderUser(newServiceProviderUser);
        currentAssignment.setStartDate(upToDateAssignment.getStartDate());
        currentAssignment.setEndDate(upToDateAssignment.getEndDate());
    }
}