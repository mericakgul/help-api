package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.core.helper.DtoMapper;
import com.mericakgul.helpapi.core.helper.UserExistence;
import com.mericakgul.helpapi.model.dto.AssignmentDto;
import com.mericakgul.helpapi.model.entity.Assignment;
import com.mericakgul.helpapi.model.entity.User;
import com.mericakgul.helpapi.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final UserExistence userExistence;
    private final UserService userService;
    private final DtoMapper dtoMapper;

    public AssignmentDto save(AssignmentDto assignmentRequest){
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String requestedServiceProviderUsername = assignmentRequest.getServiceProviderUsername();
        boolean isServiceProviderUsernameInvalid = this.isRequestedServiceProviderCurrentUser(loggedInUsername, requestedServiceProviderUsername);
        if(isServiceProviderUsernameInvalid){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The requested service provider username cannot be equal to current logged in username.");
        } else{
            User serviceProviderUser = userExistence.checkIfUserExistsAndReturn(requestedServiceProviderUsername);
            boolean isServiceProviderUserBusy = this.userService.isServiceProviderUserBusy(serviceProviderUser.getBusyPeriods(), assignmentRequest.getStartDate(), assignmentRequest.getEndDate());
            if(isServiceProviderUserBusy){
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The requested service provider is busy during the requested period.");
            } else {
                Assignment assignment = this.dtoMapper.mapModel(assignmentRequest, Assignment.class);
                assignment.setCustomerUsername(loggedInUsername);
                assignment.setIsAccepted(Boolean.FALSE);
                Assignment savedAssignment = this.assignmentRepository.save(assignment);
                return this.dtoMapper.mapModel(savedAssignment, AssignmentDto.class);
            }
        }
    }

    //RespondAssignment

    private boolean isRequestedServiceProviderCurrentUser(String loggedInUsername, String providerUsername){
        return Objects.equals(loggedInUsername, providerUsername);
    }
}
