package com.mericakgul.helpapi.core.util;

import java.time.LocalDate;

public class CompareDate {

    private CompareDate(){

    }
    public static boolean isThereOverlapBetweenDates(LocalDate firstStartDate, LocalDate firstEndDate, LocalDate secondStartDate, LocalDate secondEndDate){

        return (firstStartDate.isBefore(secondEndDate) || firstStartDate.isEqual(secondEndDate))
                                    &&
                (firstEndDate.isAfter(secondStartDate) || firstEndDate.isEqual(secondStartDate));

       // (StartA <= EndB)  and  (EndA >= StartB)
    }
}
