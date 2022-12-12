package com.mericakgul.helpapi.model.dto;

import com.mericakgul.helpapi.enums.AssignmentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AssignmentResponse {
    private Long id;
    private String description;
    private String customerUsername;
    private String serviceProviderUsername;
    private LocalDate startDate;
    private LocalDate endDate;
    private AssignmentStatus assignmentStatus;
}
