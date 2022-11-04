package com.mericakgul.helpapi.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressDto {

    private String streetName;

    private Integer houseNumber;

    private String zipCode;

    private String city;

    private String country;
}
