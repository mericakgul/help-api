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
@Table(name = "user")
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id = UUID.randomUUID();

    @NotNull
    @NotEmpty
    @Column(name = "full_name")
    private String fullName;

    @NotNull
    @NotEmpty
    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Address> addresses = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "busy_dates_id")
    )
    private List<BusyPeriod> busyPeriodList = new ArrayList<>();

    @ElementCollection(targetClass = SkillType.class)
    @Enumerated(EnumType.STRING)
    private EnumSet<SkillType> skills;

}
