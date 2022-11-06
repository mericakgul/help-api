package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.core.helper.DtoMapper;
import com.mericakgul.helpapi.model.dto.BusyPeriodDto;
import com.mericakgul.helpapi.model.entity.BusyPeriod;
import com.mericakgul.helpapi.repository.BusyPeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusyPeriodService {
    private final BusyPeriodRepository busyPeriodRepository;
    private final DtoMapper dtoMapper;

    public List<BusyPeriodDto> saveRelatedBusyPeriods(List<BusyPeriodDto> busyPeriodsRequest) {
        List<BusyPeriod> busyPeriods = this.dtoMapper.mapListModel(busyPeriodsRequest, BusyPeriod.class);
        List<BusyPeriod> savedBusyPeriods = this.busyPeriodRepository.saveAll(busyPeriods);
        return this.dtoMapper.mapListModel(savedBusyPeriods, BusyPeriodDto.class);
    }
}
