package com.mericakgul.helpapi.controller;

import com.mericakgul.helpapi.model.dto.AssignmentDto;
import com.mericakgul.helpapi.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/assignment")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping
    public AssignmentDto save(@Valid @RequestBody AssignmentDto assignmentRequest){
        return this.assignmentService.save(assignmentRequest);
    }

}
