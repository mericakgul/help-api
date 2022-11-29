package com.mericakgul.helpapi.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class BusyPeriodDto {

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

}
