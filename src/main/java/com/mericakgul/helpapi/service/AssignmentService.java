package com.mericakgul.helpapi.service;

import com.mericakgul.helpapi.core.helper.DtoMapper;
import com.mericakgul.helpapi.core.helper.ObjectExistence;
import com.mericakgul.helpapi.core.helper.ObjectUpdaterHelper;
import com.mericakgul.helpapi.enums.AssignmentStatus;
import com.mericakgul.helpapi.model.dto.AssignmentRequest;
import com.mericakgul.helpapi.model.dto.AssignmentResponse;
import com.mericakgul.helpapi.model.dto.BusyPeriodDto;
import com.mericakgul.helpapi.model.entity.Assignment;
import com.mericakgul.helpapi.model.entity.User;
import com.mericakgul.helpapi.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final ObjectExistence objectExistence;
    private final UserService userService;
    private final BusyPeriodService busyPeriodService;
    private final DtoMapper dtoMapper;
    private final ObjectUpdaterHelper objectUpdaterHelper;

    @Transactional
    public AssignmentResponse save(AssignmentRequest assignmentRequest) {
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String requestedServiceProviderUsername = assignmentRequest.getServiceProviderUsername();
        if (Objects.equals(loggedInUsername, requestedServiceProviderUsername)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The requested service provider username cannot be equal to current logged in username.");
        } else {
            User serviceProviderUser = objectExistence.checkIfUserExistsAndReturn(requestedServiceProviderUsername);
            boolean isServiceProviderUserBusy = this.userService.isServiceProviderUserBusy(serviceProviderUser.getBusyPeriods(), assignmentRequest.getStartDate(), assignmentRequest.getEndDate());
            if (isServiceProviderUserBusy) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The requested service provider is busy during the requested period.");
            } else {
                Assignment assignment = this.dtoMapper.mapModel(assignmentRequest, Assignment.class);
                this.setAssignmentUsernamesAndStatus(assignment, loggedInUsername, serviceProviderUser);
                Assignment savedAssignment = this.assignmentRepository.save(assignment);
                AssignmentResponse assignmentResponse = this.dtoMapper.mapModel(savedAssignment, AssignmentResponse.class);
                this.setAssignmentResponseUsernames(assignmentResponse, assignmentRequest.getServiceProviderUsername(), loggedInUsername);
                return assignmentResponse;
            }
        }
    }

    public List<AssignmentResponse> findByCustomerUsername(String customerUsername) {
        List<Assignment> assignments = this.assignmentRepository.findAssignmentByCustomerUser_Username(customerUsername);
        return this.createAssignmentResponseList(assignments, customerUsername, "serviceProvider");
    }

    public List<AssignmentResponse> findByServiceProviderUsername(String serviceProviderUsername) {
        List<Assignment> assignments = this.assignmentRepository.findAssignmentByServiceProviderUser_Username(serviceProviderUsername);
        return this.createAssignmentResponseList(assignments, serviceProviderUsername, "customer");
    }

    private List<AssignmentResponse> createAssignmentResponseList(List<Assignment> assignments, String customerOrProviderUsername, String otherRoleForAssignment) {
        List<AssignmentResponse> assignmentResponses = new ArrayList<>();
        if (!assignments.isEmpty()) {
            assignments.forEach(assignment -> {
                AssignmentResponse assignmentResponse = this.dtoMapper.mapModel(assignment, AssignmentResponse.class);
                if (Objects.equals(otherRoleForAssignment, "customer")) {
                    String customerUsername = assignment.getCustomerUser().getUsername();
                    this.setAssignmentResponseUsernames(assignmentResponse, customerOrProviderUsername, customerUsername);
                } else {
                    String serviceProviderUsername = assignment.getServiceProviderUser().getUsername();
                    this.setAssignmentResponseUsernames(assignmentResponse, serviceProviderUsername, customerOrProviderUsername);
                }
                assignmentResponses.add(assignmentResponse);
            });
        }
        return assignmentResponses;
    }

    public List<AssignmentResponse> findAllByUsername(String username) {
        List<AssignmentResponse> assignmentsFoundByCustomerUsername = this.findByCustomerUsername(username);
        List<AssignmentResponse> allAssignmentsOfAnUser = new ArrayList<>(assignmentsFoundByCustomerUsername);
        allAssignmentsOfAnUser.addAll(this.findByServiceProviderUsername(username));
        return allAssignmentsOfAnUser;
    }

    public void respondAssignmentById(Long id, boolean response) {
        Assignment assignment = this.objectExistence.checkIfAssignmentExistAndReturn(id);
        AssignmentStatus assignmentStatus = response ? AssignmentStatus.ACCEPTED : AssignmentStatus.REJECTED;
        if (this.isAssignmentStatusUpdatable(assignment)) {
            assignment.setAssignmentStatus(assignmentStatus);
            this.assignmentRepository.save(assignment);
            if(Objects.equals(assignmentStatus, AssignmentStatus.ACCEPTED)){
                this.updateBusyPeriodOfServiceProvider(assignment);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Either you are not authorised to change this assignment because you are not the customer or the assignment has already been responded.");
        }
    }

    // TODO
    // update edince user'in kendisini yapmayi da engelle
    // tarihlerin validligini kontrol et
    // yeni provider yeni tarihlerde uygun mu kontrol et update ederken
    // provider accept ederken de tekrar busy tarihleri kontrol etmek lazim.

    public AssignmentResponse updateAssignmentById(AssignmentRequest assignmentRequest, Long id) {
        Assignment assignment = this.objectExistence.checkIfAssignmentExistAndReturn(id);
        if (isAssignmentDeletableOrUpdatable(assignment)) {
            this.objectUpdaterHelper.updateAssignmentObjectFields(assignment, assignmentRequest);
            Assignment savedAssignment = this.assignmentRepository.save(assignment);
            AssignmentResponse assignmentResponse = this.dtoMapper.mapModel(savedAssignment, AssignmentResponse.class);
            this.setAssignmentResponseUsernames(assignmentResponse, assignmentRequest.getServiceProviderUsername(), assignment.getCustomerUser().getUsername());
            return assignmentResponse;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This assignment cannot be updated because either you are not the customer of the assignment or the assignment has already got a response.");
        }
    }

    public void deleteAssignmentById(Long id) {
        Assignment assignment = this.objectExistence.checkIfAssignmentExistAndReturn(id);
        if (isAssignmentDeletableOrUpdatable(assignment)) {
            assignment.setDeletedDate(new Date());
            this.assignmentRepository.save(assignment);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This assignment cannot be deleted because either you are not the customer of the assignment or the assignment has already got a response.");
        }
    }

    private boolean isAssignmentStatusUpdatable(Assignment assignment) {
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return Objects.equals(loggedInUsername, assignment.getServiceProviderUser().getUsername()) &&
                Objects.equals(assignment.getAssignmentStatus(), AssignmentStatus.WAITING_RESPONSE);
    }

    private boolean isAssignmentDeletableOrUpdatable(Assignment assignment) {
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return Objects.equals(loggedInUsername, assignment.getCustomerUser().getUsername()) &&
                Objects.equals(assignment.getAssignmentStatus(), AssignmentStatus.WAITING_RESPONSE);
    }

    private void updateBusyPeriodOfServiceProvider(Assignment assignment) {
        String serviceProviderUsername = assignment.getServiceProviderUser().getUsername();
        BusyPeriodDto newBusyPeriodOfServiceProvider = new BusyPeriodDto();
        newBusyPeriodOfServiceProvider.setStartDate(assignment.getStartDate());
        newBusyPeriodOfServiceProvider.setEndDate(assignment.getEndDate());
        this.busyPeriodService.saveBusyPeriodByUsername(serviceProviderUsername, newBusyPeriodOfServiceProvider);
    }

    private void setAssignmentUsernamesAndStatus(Assignment assignment, String loggedInUsername, User serviceProviderUser) {
        assignment.setCustomerUser(this.objectExistence.checkIfUserExistsAndReturn(loggedInUsername));
        assignment.setServiceProviderUser(serviceProviderUser);
        assignment.setAssignmentStatus(AssignmentStatus.WAITING_RESPONSE);
    }

    private void setAssignmentResponseUsernames(AssignmentResponse assignmentResponse, String serviceProviderUsername, String customerUsername) {
        assignmentResponse.setServiceProviderUsername(serviceProviderUsername);
        assignmentResponse.setCustomerUsername(customerUsername);
    }

}
