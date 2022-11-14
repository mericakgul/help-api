package com.mericakgul.helpapi.controller;

import com.mericakgul.helpapi.model.dto.AddressDto;
import com.mericakgul.helpapi.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public List<AddressDto> findAll() {
        return this.addressService.findAll();
    }

    @GetMapping("/{username}")
    public List<AddressDto> findAddressesByUsername(@PathVariable String username) {
        return this.addressService.findAddressesByUsername(username);
    }

    @PostMapping("/{username}")
    public AddressDto saveAddressByUsername(@Valid @RequestBody AddressDto addressRequest, @PathVariable String username) {
        return this.addressService.saveAddressByUsername(username, addressRequest);
    }

    @PutMapping
    public AddressDto updateAddressByFields(@Valid @RequestBody AddressDto upToDateAddress,
                                            @RequestParam Integer houseNumber,
                                            @RequestParam String zipCode,
                                            @RequestParam String city,
                                            @RequestParam String country) {
        return this.addressService.updateAddressByFields(houseNumber, zipCode, city, country, upToDateAddress);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAddressByFields(@RequestParam Integer houseNumber,
                                                        @RequestParam String zipCode,
                                                        @RequestParam String city,
                                                        @RequestParam String country) {
        this.addressService.deleteAddressByFields(houseNumber, zipCode, city, country);
        return ResponseEntity.ok().body("The address has been deleted.");
    }
}
