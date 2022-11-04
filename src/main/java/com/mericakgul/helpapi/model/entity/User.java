package com.mericakgul.helpapi.model.entity;

import com.mericakgul.helpapi.core.util.DeletedDateUtil;
import com.mericakgul.helpapi.enums.SkillType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
@Setter
@Getter
@Where(clause = "DELETED_DATE = '1970-01-01 00:00:00.000'") // For Soft deleting
public class User implements UserDetails {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID uuid;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "deleted_date")
    private Date deletedDate = DeletedDateUtil.getDefaultDeletedDate();

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private List<Address> addresses = new ArrayList<>();

    @ElementCollection(targetClass = SkillType.class)
    @Enumerated(EnumType.STRING)
    private List<SkillType> skills;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "busy_period_id")
    )
    private List<BusyPeriod> busyPeriods = new ArrayList<>();



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
