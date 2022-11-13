package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.core.helper.DtoMapper;
import com.mericakgul.helpapi.core.helper.UserExistence;
import com.mericakgul.helpapi.model.dto.BusyPeriodDto;
import com.mericakgul.helpapi.model.entity.BusyPeriod;
import com.mericakgul.helpapi.model.entity.User;
import com.mericakgul.helpapi.repository.BusyPeriodRepository;
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
public class BusyPeriodService {
    private final BusyPeriodRepository busyPeriodRepository;
    private final DtoMapper dtoMapper;
    private final UserExistence userExistence;

    public List<BusyPeriodDto> findAll() {
        List<BusyPeriod> busyPeriods = this.busyPeriodRepository.findAll();
        if (busyPeriods.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no busy period saved yet.");
        } else {
            return this.dtoMapper.mapListModel(busyPeriods, BusyPeriodDto.class);
        }
    }

    public List<BusyPeriodDto> findBusyPeriodsByUsername(String username){
        User user = userExistence.checkIfUserExistsAndReturn(username);
        List<BusyPeriod> busyPeriodsOfUser = user.getBusyPeriods();
        return this.dtoMapper.mapListModel(busyPeriodsOfUser, BusyPeriodDto.class);
    }

    public List<BusyPeriod> saveAll(List<BusyPeriod> busyPeriodsFromUserRequest) {
        if (busyPeriodsFromUserRequest == null) {
            return null;
        } else {
            List<BusyPeriod> busyPeriodsToSetUser = new ArrayList<>();
            busyPeriodsFromUserRequest.forEach(busyPeriod ->
                    busyPeriodsToSetUser.add(checkIfBusyPeriodAlreadyExistsAndReturn(busyPeriod))
            );
            return busyPeriodsToSetUser;
        }
    }


    @Transactional
    public BusyPeriodDto saveBusyPeriodByUsername(String username, BusyPeriodDto busyPeriodRequest) {
        User user = userExistence.checkIfUserExistsAndReturn(username);
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if(Objects.equals(loggedInUsername, username)){
            return this.assignBusyPeriodToUser(user, busyPeriodRequest);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorised to add busy period to the user: " + username);
        }
    }

    @Transactional
    public void deleteRelatedBusyPeriods(List<BusyPeriod> busyPeriodsOfUserToDelete) {
        busyPeriodsOfUserToDelete.forEach(busyPeriod -> {
            List<User> usersThatHasThisBusyPeriod = busyPeriod.getUsers();
            if (usersThatHasThisBusyPeriod.size() < 2) {
                busyPeriod.setDeletedDate(new Date());
            }
        });
    }

    private BusyPeriodDto assignBusyPeriodToUser(User user, BusyPeriodDto busyPeriodRequest){
        if(this.doesUserAlreadyHaveTheBusyPeriod(user, busyPeriodRequest)){
            throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, "This user already has this busy period.");
        } else{
            BusyPeriod busyPeriod = this.dtoMapper.mapModel(busyPeriodRequest, BusyPeriod.class);
            BusyPeriod busyPeriodToSetUser = this.checkIfBusyPeriodAlreadyExistsAndReturn(busyPeriod);
            busyPeriodToSetUser.getUsers().add(user);
            user.getBusyPeriods().add(busyPeriodToSetUser);
            return this.dtoMapper.mapModel(busyPeriodToSetUser, BusyPeriodDto.class);
        }
    }

    private BusyPeriod checkIfBusyPeriodAlreadyExistsAndReturn(BusyPeriod busyPeriod){
        Optional<BusyPeriod> busyPeriodOptional = this.busyPeriodRepository
                .findBusyPeriodByStartDateAndEndDate(busyPeriod.getStartDate(), busyPeriod.getEndDate());
        return busyPeriodOptional.orElseGet(() -> this.busyPeriodRepository.save(busyPeriod));
    }

    private boolean doesUserAlreadyHaveTheBusyPeriod(User user, BusyPeriodDto busyPeriodRequest){
        List<BusyPeriod> busyPeriodsOfUser = user.getBusyPeriods();
        Optional<BusyPeriod> busyPeriodInUser = busyPeriodsOfUser
                .stream()
                .filter(busyPeriod ->
                    busyPeriod.getStartDate().isEqual(busyPeriodRequest.getStartDate()) &&
                            busyPeriod.getEndDate().isEqual(busyPeriodRequest.getEndDate())
                ).findAny();
        return busyPeriodInUser.isPresent();
    }
}
