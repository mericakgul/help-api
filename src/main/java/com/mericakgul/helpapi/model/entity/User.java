package com.mericakgul.helpapi.model.entity;

import com.mericakgul.helpapi.enums.SkillType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "users")
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id = UUID.randomUUID();

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Address> addresses = new ArrayList<>();

    @ElementCollection(targetClass = SkillType.class)
    @Enumerated(EnumType.STRING)
    private List<SkillType> skills;

}
