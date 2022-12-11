package com.mericakgul.helpapi.model.dto;

import com.mericakgul.helpapi.enums.SkillType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ServiceProviderFinderDto {

    @NotNull
    private SkillType skill;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;
}
