package com.mericakgul.helpapi.controller;

import com.mericakgul.helpapi.model.dto.AssignmentRequest;
import com.mericakgul.helpapi.model.dto.AssignmentResponse;
import com.mericakgul.helpapi.model.dto.RespondAssignmentDto;
import com.mericakgul.helpapi.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/assignment")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping
    public AssignmentResponse save(@Valid @RequestBody AssignmentRequest assignmentRequest){
        return this.assignmentService.save(assignmentRequest);
    }

    @GetMapping
    public List<AssignmentResponse> findAllByLoggedInUsername(){
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.assignmentService.findAllByUsername(loggedInUsername);
    }
    @GetMapping("/customer")
    public List<AssignmentResponse> findAllByLoggedInUsernameAsCustomer(){
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.assignmentService.findByCustomerUsername(loggedInUsername);
    }
    @GetMapping("/serviceProvider")
    public List<AssignmentResponse> findAllByLoggedInUsernameAsServiceProvider(){
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.assignmentService.findByServiceProviderUsername(loggedInUsername);
    }

    @GetMapping("/customer/{customerUsername}")
    public List<AssignmentResponse> findAllByCustomerUsername(@PathVariable String customerUsername){
        return this.assignmentService.findByCustomerUsername(customerUsername);
    }

    @GetMapping("/serviceProvider/{serviceProviderUsername}")
    public List<AssignmentResponse> findAllByServiceProviderUsername(@PathVariable String serviceProviderUsername){
        return this.assignmentService.findByServiceProviderUsername(serviceProviderUsername);
    }

    @GetMapping("/{username}")
    public List<AssignmentResponse> findAllByUsername(@PathVariable String username){
        return this.assignmentService.findAllByUsername(username);
    }

    @PostMapping("/response")
    public ResponseEntity<String> respondAssignmentById(@Valid @RequestBody RespondAssignmentDto respondAssignmentDto){
        this.assignmentService.respondAssignmentById(respondAssignmentDto.getId(), respondAssignmentDto.isResponse());
        return ResponseEntity.ok().body("The assignment has been updated with your response.");
    }

    @PutMapping("/{id}")
    public AssignmentResponse updateAssignmentById(@Valid @RequestBody AssignmentRequest assignmentRequest, @PathVariable Long id){
        return this.assignmentService.updateAssignmentById(assignmentRequest, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAssignmentById(@PathVariable Long id){
        this.assignmentService.deleteAssignmentById(id);
        return ResponseEntity.ok().body("The assignment has been deleted.");
    }
}
