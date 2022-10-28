package com.mericakgul.helpapi.model.dto;

import com.mericakgul.helpapi.model.entity.Address;
import com.mericakgul.helpapi.model.entity.BusyPeriod;
import com.mericakgul.helpapi.model.entity.Skill;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
    private List<Address> addresses;
    private String description;
}
