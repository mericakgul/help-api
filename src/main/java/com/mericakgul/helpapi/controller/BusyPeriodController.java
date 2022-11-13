package com.mericakgul.helpapi.controller;

import com.mericakgul.helpapi.model.dto.BusyPeriodDto;
import com.mericakgul.helpapi.service.BusyPeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/busyPeriod")
@RequiredArgsConstructor
public class BusyPeriodController {

    private final BusyPeriodService busyPeriodService;

    @GetMapping
    public List<BusyPeriodDto> findAll(){
        return this.busyPeriodService.findAll();
    }

    @GetMapping("/{username}")
    public List<BusyPeriodDto> findBusyPeriodsByUsername(@PathVariable String username){
        return this.busyPeriodService.findBusyPeriodsByUsername(username);
    }

}
