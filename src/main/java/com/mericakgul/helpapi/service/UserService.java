package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.core.helper.DtoMapper;
import com.mericakgul.helpapi.core.helper.ObjectUpdaterHelper;
import com.mericakgul.helpapi.core.helper.UserExistence;
import com.mericakgul.helpapi.model.dto.UserRequest;
import com.mericakgul.helpapi.model.dto.UserResponse;
import com.mericakgul.helpapi.model.entity.Address;
import com.mericakgul.helpapi.model.entity.BusyPeriod;
import com.mericakgul.helpapi.model.entity.User;
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
    private final AddressService addressService;
    private final BusyPeriodService busyPeriodService;
    private final DtoMapper dtoMapper;
    private final ObjectUpdaterHelper objectUpdaterHelper;

    private final UserExistence userExistence;
    public List<UserResponse> findAll() {
        List<User> userList = this.userRepository.findAll();
        return this.dtoMapper.mapListModel(userList, UserResponse.class);
        // Note: To be able to make a request to this end point, a token is needed. If there is a valid token,
        // which means there is at least one user in the db for sure. Therefore, the check below is removed.
//        if(userList.isEmpty()){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is not any user yet.");
//        } else {
//            return this.dtoMapper.mapListModel(userList, UserResponse.class);
//        }
    }
    public UserResponse findByUsername(String username) {
        User user = userExistence.checkIfUserExistsAndReturn(username);
        return this.dtoMapper.mapModel(user, UserResponse.class);
    }

    @Transactional
    public void deleteByUsername(String username) {
        User user = userExistence.checkIfUserExistsAndReturn(username);
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName(); // to retrieve password getPrincipal()
        if(Objects.equals(username, loggedInUsername)){
            this.deleteUserRelations(user);
            user.setDeletedDate(new Date());
            this.userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorised to delete the user: " + username);
            // UNAUTHORIZED exception doesn't show the message, how can I get it shown?
        }
    }

    private void deleteUserRelations(User user){
        this.addressService.deleteRelatedAddresses(user.getUserUuid());
        this.busyPeriodService.deleteRelatedBusyPeriods(user.getBusyPeriods());
    }

    @Transactional
    public UserResponse update(String username, UserRequest userRequest) {
        Optional<User> optionalUser = this.userRepository.findByUsername(username);
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (optionalUser.isPresent() && Objects.equals(username, loggedInUsername)){
            User currentUser = optionalUser.get();
            this.updateUserWithRelations(currentUser, userRequest);
            return this.dtoMapper.mapModel(currentUser, UserResponse.class);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user found with this id to update or you are not authorized to update this user.");
        }
    }

    private void updateUserWithRelations(User currentUser, UserRequest userRequest){
        User upToDateUser = this.dtoMapper.mapModel(userRequest, User.class);
        this.objectUpdaterHelper.updateUserObjectPrimitiveFields(currentUser, upToDateUser);
        this.deleteUserRelations(currentUser);
        List<Address> savedAddresses = this.addressService.saveAll(upToDateUser.getAddresses());
        currentUser.setAddresses(savedAddresses);
        List<BusyPeriod> savedBusyPeriods = this.busyPeriodService.saveAll(upToDateUser.getBusyPeriods());
        currentUser.setBusyPeriods(savedBusyPeriods);
        /* NOTE: Here while we are updating a user, we first delete its relations Addresses and Busy dates.
        Afterwards, we save the new ones in the request, but we do not check if the busy periods or addresses are the same as before.
        So this is not the best for performance wise because there will be extra and redundant processes
        in case there is no update in addresses and busy periods while updating a user.
         */
    }
}