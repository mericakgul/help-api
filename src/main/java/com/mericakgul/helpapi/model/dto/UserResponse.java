package com.mericakgul.helpapi.model.dto;

import com.mericakgul.helpapi.enums.SkillType;
import com.mericakgul.helpapi.model.entity.Address;
import com.mericakgul.helpapi.model.entity.BusyPeriod;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {

    private UUID id;

    private String username;

    private String email;

    private String fullName;

    private String phoneNumber;

    private String description;

    private List<AddressDto> addresses;

    private List<SkillType> skills;

    private List<BusyPeriod> busyPeriods;
}
