package com.mericakgul.helpapi.controller;

import com.mericakgul.helpapi.model.dto.BusyPeriodDto;
import com.mericakgul.helpapi.service.BusyPeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/busyperiod")
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

    @PostMapping("/{username}")
    public BusyPeriodDto saveBusyPeriodByUsername(@PathVariable String username, @RequestBody BusyPeriodDto busyPeriodRequest){
        return this.busyPeriodService.saveBusyPeriodByUsername(username, busyPeriodRequest);
    }

}
