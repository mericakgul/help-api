package com.mericakgul.helpapi.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class BusyPeriodDto {

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

}
