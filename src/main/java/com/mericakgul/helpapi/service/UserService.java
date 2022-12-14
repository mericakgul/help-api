package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.core.helper.DtoMapper;
import com.mericakgul.helpapi.core.helper.ObjectUpdaterHelper;
import com.mericakgul.helpapi.core.helper.UserExistence;
import com.mericakgul.helpapi.core.util.CompareDates;
import com.mericakgul.helpapi.enums.SkillType;
import com.mericakgul.helpapi.model.dto.ServiceProviderFinderDto;
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

import java.time.LocalDate;
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

    public List<UserResponse> findServiceProvidersBySkillAndBusyPeriod(ServiceProviderFinderDto serviceProviderFinderDto) {
        List<User> allUsers = this.userRepository.findAll();
        List<User> usersWhoHaveTheSkill = this.filterUsersBySkill(allUsers, serviceProviderFinderDto.getSkill());
        List<User> usersWhoAreAvailable = this.filterUsersByAvailability(usersWhoHaveTheSkill, serviceProviderFinderDto);
        if(usersWhoAreAvailable.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no service provider user available with this skill for these dates.");
        } else {
            return this.dtoMapper.mapListModel(usersWhoAreAvailable, UserResponse.class);
        }
    }

    @Transactional
    public UserResponse update(String username, UserRequest userRequest) {
        User user = userExistence.checkIfUserExistsAndReturn(username);
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (Objects.equals(username, loggedInUsername)) {
            this.updateUserWithRelations(user, userRequest);
            return this.dtoMapper.mapModel(user, UserResponse.class);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user found with this id to update or you are not authorized to update this user.");
        }
    }

    @Transactional
    public void deleteByUsername(String username) {
        User user = userExistence.checkIfUserExistsAndReturn(username);
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName(); // to retrieve password getPrincipal()
        if (Objects.equals(username, loggedInUsername)) {
            this.deleteUserRelations(user);
            user.setDeletedDate(new Date());
            this.userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorised to delete the user: " + username);
            // UNAUTHORIZED exception doesn't show the message, how can I get it shown?
        }
    }

    private void deleteUserRelations(User user) {
        this.addressService.deleteRelatedAddresses(user.getUserUuid());
        this.busyPeriodService.deleteRelatedBusyPeriods(user.getBusyPeriods());
    }

    private void updateUserWithRelations(User currentUser, UserRequest userRequest) {
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

    private List<User> filterUsersBySkill(List<User> users, SkillType skill) {
        return users.stream()
                .filter(user -> user.getSkills().contains(skill))
                .toList();
    }
    private List<User> filterUsersByAvailability(List<User> users, ServiceProviderFinderDto serviceProviderFinderDto){
        LocalDate requestedStartDate = serviceProviderFinderDto.getStartDate();
        LocalDate requestedEndDate = serviceProviderFinderDto.getEndDate();
        return users.stream()
                .filter(serviceProviderUser -> !this.isServiceProviderUserBusy(serviceProviderUser.getBusyPeriods(), requestedStartDate, requestedEndDate))
                .toList();
    }
    boolean isServiceProviderUserBusy(List<BusyPeriod> busyPeriods, LocalDate requestedStartDate, LocalDate requestedEndDate){
        Optional<BusyPeriod> overlapBusyPeriod = busyPeriods.stream()
                .filter(busyPeriod ->
                    CompareDates.areDatesValid(requestedStartDate, requestedEndDate) &&
                    CompareDates.isThereOverlapBetweenDates(busyPeriod.getStartDate(), busyPeriod.getEndDate(), requestedStartDate, requestedEndDate))
                .findFirst();
        return overlapBusyPeriod.isPresent();
    }
}
