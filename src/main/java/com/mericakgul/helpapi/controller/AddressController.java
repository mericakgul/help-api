package com.mericakgul.helpapi.controller;

import com.mericakgul.helpapi.model.dto.AddressDto;
import com.mericakgul.helpapi.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public List<AddressDto> findAll(){
        return this.addressService.findAll();
    }
    @GetMapping("/{username}")
    public List<AddressDto> findAddressesByUsername(@PathVariable String username){
        return this.addressService.findAddressesByUsername(username);
    }
}
