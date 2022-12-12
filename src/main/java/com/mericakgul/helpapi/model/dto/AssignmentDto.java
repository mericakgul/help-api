package com.mericakgul.helpapi.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AssignmentDto {

    private Long id;

    @NotNull
    @NotEmpty
    private String description;

    @NotNull
    @NotEmpty
    private String serviceProviderUsername;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

}
