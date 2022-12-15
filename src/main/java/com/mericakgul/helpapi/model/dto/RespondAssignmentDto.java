package com.mericakgul.helpapi.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class RespondAssignmentDto {

    @NotNull
    private Long id;

    @NotNull
    private boolean response;
}
