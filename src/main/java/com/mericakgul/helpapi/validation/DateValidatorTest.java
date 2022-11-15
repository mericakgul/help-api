package com.mericakgul.helpapi.validation;

import com.mericakgul.helpapi.model.dto.BusyPeriodDto;
import com.mericakgul.helpapi.model.entity.BusyPeriod;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

public class DateValidatorTest {

    public static boolean isValid(BusyPeriodDto busyPeriodDto) {
        LocalDate startDate = busyPeriodDto.getStartDate();
        LocalDate endDate = busyPeriodDto.getEndDate();
        return areDatesValid(startDate, endDate);
    }

    public static boolean isValid(BusyPeriod busyPeriod) {
        LocalDate startDate = busyPeriod.getStartDate();
        LocalDate endDate = busyPeriod.getEndDate();
        return areDatesValid(startDate, endDate);
    }

    private static boolean areDatesValid(LocalDate startDate, LocalDate endDate){
        LocalDate today = LocalDate.now();
        boolean result = startDate.isAfter(today)
                && endDate.isBefore(today.plusYears(2))
                && (endDate.isAfter(startDate) || endDate.isEqual(startDate));
        if(result){
            return true;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid date.");
        }
    }
}
