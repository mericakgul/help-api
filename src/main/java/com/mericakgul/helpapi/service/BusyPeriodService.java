package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.core.helper.DtoMapper;
import com.mericakgul.helpapi.model.dto.BusyPeriodDto;
import com.mericakgul.helpapi.model.entity.BusyPeriod;
import com.mericakgul.helpapi.model.entity.User;
import com.mericakgul.helpapi.repository.BusyPeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusyPeriodService {
    private final BusyPeriodRepository busyPeriodRepository;
    private final DtoMapper dtoMapper;
    List<BusyPeriod> busyPeriodsToSetUser = new ArrayList<>();

    public List<BusyPeriod> saveRelatedBusyPeriods(List<BusyPeriod> busyPeriodsFromUserRequest) {
        busyPeriodsToSetUser.clear();
        busyPeriodsFromUserRequest.forEach(this::buildBusyPeriodsToSetUser);
        return busyPeriodsToSetUser;
    }

    public void buildBusyPeriodsToSetUser(BusyPeriod busyPeriod) {
        Optional<BusyPeriod> busyPeriodOptional = this.busyPeriodRepository
                .findBusyPeriodByStartDateAndEndDate(busyPeriod.getStartDate(), busyPeriod.getEndDate());
        if (busyPeriodOptional.isPresent()) {
            busyPeriodsToSetUser.add(busyPeriodOptional.get());
        } else {
            BusyPeriod savedBusyPeriod = this.busyPeriodRepository.save(busyPeriod);
            busyPeriodsToSetUser.add(savedBusyPeriod);
        }
    }

    public void deleteRelatedBusyPeriods(List<BusyPeriod> busyPeriodsOfUserToDelete) {
        busyPeriodsOfUserToDelete.forEach(busyPeriod -> {
            List<User> usersThatHasThisBusyPeriod = this.busyPeriodRepository.findUsersByBusyPeriodId(busyPeriod.getId());
            if (usersThatHasThisBusyPeriod.size() < 2) {
                busyPeriod.setDeletedDate(new Date());
            }
        });
    }

    public List<BusyPeriodDto> findAll() {
        List<BusyPeriod> busyPeriods = this.busyPeriodRepository.findAll();
        if (busyPeriods.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no busy period saved yet.");
        } else {
            return this.dtoMapper.mapListModel(busyPeriods, BusyPeriodDto.class);
        }
    }
}
