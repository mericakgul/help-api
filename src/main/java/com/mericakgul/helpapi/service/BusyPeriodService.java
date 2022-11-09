package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.model.entity.BusyPeriod;
import com.mericakgul.helpapi.repository.BusyPeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusyPeriodService {
    private final BusyPeriodRepository busyPeriodRepository;
    List<BusyPeriod> busyPeriodsToSetUser = new ArrayList<>();
    public List<BusyPeriod> saveRelatedBusyPeriods(List<BusyPeriod> busyPeriodsFromUserRequest) {
        busyPeriodsToSetUser.clear();
        busyPeriodsFromUserRequest.forEach(this::buildBusyPeriodsToSetUser);
        return busyPeriodsToSetUser;
    }

    public void buildBusyPeriodsToSetUser(BusyPeriod busyPeriod){
        Optional<BusyPeriod> busyPeriodOptional = this.busyPeriodRepository
                .findBusyPeriodByStartDateAndEndDate(busyPeriod.getStartDate(), busyPeriod.getEndDate());
        if (busyPeriodOptional.isPresent()) {
            busyPeriodsToSetUser.add(busyPeriodOptional.get());
        } else{
            BusyPeriod savedBusyPeriod = this.busyPeriodRepository.save(busyPeriod);
            busyPeriodsToSetUser.add(savedBusyPeriod);
        }
    }
}
