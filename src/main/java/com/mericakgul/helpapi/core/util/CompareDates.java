package com.mericakgul.helpapi.core.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

public class CompareDates {

    private CompareDates(){

    }
    public static boolean isThereOverlapBetweenDates(LocalDate firstStartDate, LocalDate firstEndDate, LocalDate secondStartDate, LocalDate secondEndDate){

        return (firstStartDate.isBefore(secondEndDate) || firstStartDate.isEqual(secondEndDate))
                                    &&
                (firstEndDate.isAfter(secondStartDate) || firstEndDate.isEqual(secondStartDate));

       // (StartA <= EndB)  and  (EndA >= StartB)
    }

    public static boolean areDatesValid(LocalDate startDate, LocalDate endDate){
        //Using this method makes the code a bit unreadable. Would be nice if there is a shorter version of adding date validation, sth like creating own annotation validation like @NotNull.
        LocalDate today = LocalDate.now();

        if(startDate.isBefore(today)){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Start date cannot be in the past");
        } else if(endDate.isBefore(startDate)){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "End date cannot be before start date");
        } else if (startDate.isAfter(today.plusYears(2))){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The start date cannot be beyond two years from today.");
        } else return true;
    }
}
