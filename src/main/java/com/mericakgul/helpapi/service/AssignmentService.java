package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.core.helper.DtoMapper;
import com.mericakgul.helpapi.core.helper.UserExistence;
import com.mericakgul.helpapi.enums.AssignmentStatus;
import com.mericakgul.helpapi.model.dto.AssignmentRequest;
import com.mericakgul.helpapi.model.dto.AssignmentResponse;
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

    public AssignmentResponse save(AssignmentRequest assignmentRequest){
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String requestedServiceProviderUsername = assignmentRequest.getServiceProviderUsername();
        boolean isRequestedServiceProviderSameAsLoggedInUser = this.isRequestedServiceProviderSameAsLoggedInUser(loggedInUsername, requestedServiceProviderUsername);
        if(isRequestedServiceProviderSameAsLoggedInUser){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The requested service provider username cannot be equal to current logged in username.");
        } else{
            User serviceProviderUser = userExistence.checkIfUserExistsAndReturn(requestedServiceProviderUsername);
            boolean isServiceProviderUserBusy = this.userService.isServiceProviderUserBusy(serviceProviderUser.getBusyPeriods(), assignmentRequest.getStartDate(), assignmentRequest.getEndDate());
            if(isServiceProviderUserBusy){
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The requested service provider is busy during the requested period.");
            } else {
                Assignment assignment = this.dtoMapper.mapModel(assignmentRequest, Assignment.class);
                this.updateAssignmentWithRelations(assignment, loggedInUsername, serviceProviderUser);
                Assignment savedAssignment = this.assignmentRepository.save(assignment);
                AssignmentResponse assignmentResponse = this.dtoMapper.mapModel(savedAssignment, AssignmentResponse.class);
                this.updateAssignmentResponseWithRelations(assignmentResponse, assignmentRequest, loggedInUsername);
                return assignmentResponse;
            }
        }
    }

    //RespondAssignment

    private boolean isRequestedServiceProviderSameAsLoggedInUser(String loggedInUsername, String providerUsername){
        return Objects.equals(loggedInUsername, providerUsername);
    }

    private void updateAssignmentWithRelations(Assignment assignment, String loggedInUsername, User serviceProviderUser){
        assignment.setCustomerUser(this.userExistence.checkIfUserExistsAndReturn(loggedInUsername));
        assignment.setServiceProviderUser(serviceProviderUser);
        assignment.setAssignmentStatus(AssignmentStatus.WAITING_RESPONSE);
    }
    private void updateAssignmentResponseWithRelations(AssignmentResponse assignmentResponse, AssignmentRequest assignmentRequest, String loggedInUsername){
        assignmentResponse.setServiceProviderUsername(assignmentRequest.getServiceProviderUsername());
        assignmentResponse.setCustomerUsername(loggedInUsername);
    }
}
