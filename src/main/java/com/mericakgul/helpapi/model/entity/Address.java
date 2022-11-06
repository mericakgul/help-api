package com.mericakgul.helpapi.model.entity;

import com.mericakgul.helpapi.core.util.DeletedDateUtil;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "address")
@Getter
@Setter
@Where(clause = "DELETED_DATE = '1970-01-01 00:00:00.000'") // For Soft deleting
public class Address {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID addressUuid;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "house_number")
    private Integer houseNumber;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid", insertable = false, updatable = false)
    private User user;

    @Column(name = "deleted_date")
    private Date deletedDate = DeletedDateUtil.getDefaultDeletedDate();
}
