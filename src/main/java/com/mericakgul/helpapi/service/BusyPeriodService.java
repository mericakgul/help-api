package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.core.helper.DtoMapper;
import com.mericakgul.helpapi.core.helper.ObjectExistence;
import com.mericakgul.helpapi.core.util.CompareDates;
import com.mericakgul.helpapi.model.dto.BusyPeriodDto;
import com.mericakgul.helpapi.model.entity.BusyPeriod;
import com.mericakgul.helpapi.model.entity.User;
import com.mericakgul.helpapi.repository.BusyPeriodRepository;
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
public class BusyPeriodService {
    private final BusyPeriodRepository busyPeriodRepository;
    private final DtoMapper dtoMapper;
    private final ObjectExistence objectExistence;

    public List<BusyPeriodDto> findAll() {
        List<BusyPeriod> busyPeriods = this.busyPeriodRepository.findAll();
        if (busyPeriods.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no busy period saved yet.");
        } else {
            return this.dtoMapper.mapListModel(busyPeriods, BusyPeriodDto.class);
        }
    }

    public List<BusyPeriodDto> findBusyPeriodsByUsername(String username) {
        User user = objectExistence.checkIfUserExistsAndReturn(username);
        List<BusyPeriod> busyPeriodsOfUser = user.getBusyPeriods();
        return this.dtoMapper.mapListModel(busyPeriodsOfUser, BusyPeriodDto.class);
    }

    @Transactional
    public List<BusyPeriod> saveAll(List<BusyPeriod> busyPeriodsFromUserRequest) {
        if (busyPeriodsFromUserRequest == null) {
            return null;
        } else {
            List<BusyPeriod> busyPeriodsToSetUser = new ArrayList<>();
            busyPeriodsFromUserRequest.forEach(busyPeriod -> {
                if (CompareDates.areDatesValid(busyPeriod.getStartDate(), busyPeriod.getEndDate()) &&
                        !this.areBusyPeriodsOverlap(busyPeriodsToSetUser, busyPeriod)) {
                    busyPeriodsToSetUser.add(checkIfBusyPeriodAlreadyExistsAndReturn(busyPeriod));
                }
            });
            return busyPeriodsToSetUser;
        }
    }

    @Transactional
    public BusyPeriodDto saveBusyPeriodByUsername(String username, BusyPeriodDto busyPeriodRequest) {
        User user = objectExistence.checkIfUserExistsAndReturn(username);
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (Objects.equals(loggedInUsername, username) &&
                CompareDates.areDatesValid(busyPeriodRequest.getStartDate(), busyPeriodRequest.getEndDate())) {
            BusyPeriod busyPeriodToAssignToUser = this.dtoMapper.mapModel(busyPeriodRequest, BusyPeriod.class);
            return this.assignBusyPeriodToUser(user, busyPeriodToAssignToUser);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorised to add busy period to the user: " + username);
        }
    }

    @Transactional
    public BusyPeriodDto updateBusyPeriodByFields(LocalDate startDate, LocalDate endDate, BusyPeriodDto upToDateBusyPeriod) {
        BusyPeriod currentBusyPeriod = this.objectExistence.checkIfBusyPeriodExistsAndReturn(startDate, endDate);
        if (this.isLoggedInUserAuthorisedToChangeBusyPeriod(currentBusyPeriod) &&
                CompareDates.areDatesValid(upToDateBusyPeriod.getStartDate(), upToDateBusyPeriod.getEndDate())) {
            currentBusyPeriod.setStartDate(upToDateBusyPeriod.getStartDate());
            currentBusyPeriod.setEndDate(upToDateBusyPeriod.getEndDate());
            return this.dtoMapper.mapModel(currentBusyPeriod, BusyPeriodDto.class);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This busy period is used by another user so cannot be updated. Update your account with desired busy period.");
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

    @Transactional
    public void deleteBusyPeriodByFields(LocalDate startDate, LocalDate endDate) {
        BusyPeriod busyPeriodToDelete = this.objectExistence.checkIfBusyPeriodExistsAndReturn(startDate, endDate);
        if (this.isLoggedInUserAuthorisedToChangeBusyPeriod(busyPeriodToDelete)) {
            busyPeriodToDelete.setDeletedDate(new Date());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This busy period is used by another user so it cannot be deleted.");
        }
    }

    private BusyPeriodDto assignBusyPeriodToUser(User user, BusyPeriod busyPeriodComingFromRequest) {
        boolean doesUserHaveTheSameBusyPeriod = this.doesUserAlreadyHaveTheBusyPeriod(user, busyPeriodComingFromRequest);
        boolean isThereOverlap = this.areBusyPeriodsOverlap(user.getBusyPeriods(), busyPeriodComingFromRequest);
        BusyPeriod busyPeriodToSetUser = this.checkIfBusyPeriodAlreadyExistsAndReturn(busyPeriodComingFromRequest);
        if (!doesUserHaveTheSameBusyPeriod && !isThereOverlap) {
            user.getBusyPeriods().add(busyPeriodToSetUser);
        }
        return this.dtoMapper.mapModel(busyPeriodToSetUser, BusyPeriodDto.class);
    }

    private BusyPeriod checkIfBusyPeriodAlreadyExistsAndReturn(BusyPeriod busyPeriod) {
        Optional<BusyPeriod> busyPeriodOptional = this.busyPeriodRepository
                .findBusyPeriodByStartDateAndEndDate(busyPeriod.getStartDate(), busyPeriod.getEndDate());
        return busyPeriodOptional.orElseGet(() -> this.busyPeriodRepository.save(busyPeriod));
    }

    private boolean doesUserAlreadyHaveTheBusyPeriod(User user, BusyPeriod busyPeriodRequest) {
        List<BusyPeriod> busyPeriodsOfUser = user.getBusyPeriods();
        Optional<BusyPeriod> busyPeriodInUser = busyPeriodsOfUser
                .stream()
                .filter(busyPeriod ->
                        busyPeriod.getStartDate().isEqual(busyPeriodRequest.getStartDate()) &&
                                busyPeriod.getEndDate().isEqual(busyPeriodRequest.getEndDate())
                ).findAny();
        if (busyPeriodInUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED, "Already busy in this period.");
        } else return false;

    }

    private boolean isLoggedInUserAuthorisedToChangeBusyPeriod(BusyPeriod busyPeriodToDeleteOrUpdate) {
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        List<User> users = busyPeriodToDeleteOrUpdate.getUsers();
        return users.size() == 1 && users.get(0).getUsername().equals(loggedInUsername);
    }

    private boolean areBusyPeriodsOverlap(List<BusyPeriod> busyPeriodsOfUser, BusyPeriod newBusyPeriod) {
        Optional<BusyPeriod> overlappedBusyPeriod = busyPeriodsOfUser.stream()
                .filter(busyPeriodOfUser ->
                        CompareDates.isThereOverlapBetweenDates(busyPeriodOfUser.getStartDate(), busyPeriodOfUser.getEndDate(), newBusyPeriod.getStartDate(), newBusyPeriod.getEndDate()))
                .findFirst();
        if (overlappedBusyPeriod.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Busy periods are overlapped.");
        } else return false;
    }
}
