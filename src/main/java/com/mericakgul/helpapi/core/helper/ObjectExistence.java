package com.mericakgul.helpapi.core.helper;

import com.mericakgul.helpapi.model.entity.Assignment;
import com.mericakgul.helpapi.model.entity.BusyPeriod;
import com.mericakgul.helpapi.model.entity.User;
import com.mericakgul.helpapi.repository.AssignmentRepository;
import com.mericakgul.helpapi.repository.BusyPeriodRepository;
import com.mericakgul.helpapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ObjectExistence {
    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;

    private final BusyPeriodRepository busyPeriodRepository;

    public User checkIfUserExistsAndReturn(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user found with this username."));
    }

    public BusyPeriod checkIfBusyPeriodExistsAndReturn(LocalDate startDate, LocalDate endDate){
        return this.busyPeriodRepository.findBusyPeriodByStartDateAndEndDate(startDate, endDate)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no busy period found with these dates."));
    }

    public Assignment checkIfAssignmentExistAndReturn(Long id) {
        return this.assignmentRepository.findAssignmentById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no assignment found with this id."));
    }
}
