package com.mericakgul.helpapi.validation;

import com.mericakgul.helpapi.model.dto.BusyPeriodDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<ValidDates, BusyPeriodDto> {

    @Override
    public void initialize(ValidDates constraintAnnotation) {
    }

    @Override
    public boolean isValid(BusyPeriodDto busyPeriodDto, ConstraintValidatorContext context) {
        LocalDate startDate = busyPeriodDto.getStartDate();
        LocalDate endDate = busyPeriodDto.getEndDate();
        LocalDate today = LocalDate.now();

        return startDate.isAfter(today)
                && endDate.isBefore(today.plusYears(2))
                && (endDate.isAfter(startDate) || endDate.isEqual(startDate));

    }

}
