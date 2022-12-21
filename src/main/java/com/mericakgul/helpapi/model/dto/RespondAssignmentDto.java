package com.mericakgul.helpapi.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(description = "This model dto is used by service provider users to respond an existing assignment on their name as service provider.")
public class RespondAssignmentDto {

    @NotNull
    private Long id;

    @NotNull
    private boolean response;
}
