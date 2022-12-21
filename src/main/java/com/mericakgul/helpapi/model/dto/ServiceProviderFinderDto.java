package com.mericakgul.helpapi.model.dto;

import com.mericakgul.helpapi.enums.SkillType;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(description = "This model dto is used to make a request which lists all the available service provider users according to skill and available time period.")
public class ServiceProviderFinderDto {

    @NotNull
    private SkillType skill;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
