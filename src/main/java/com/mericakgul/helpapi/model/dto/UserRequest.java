package com.mericakgul.helpapi.model.dto;

import com.mericakgul.helpapi.enums.SkillType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserRequest {

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String fullName;

    @NotNull
    @NotEmpty
    private List<AddressDto> addresses;

    private String phoneNumber;

    private String description;

    private List<SkillType> skills;
}
