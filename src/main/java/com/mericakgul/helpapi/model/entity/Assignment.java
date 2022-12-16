package com.mericakgul.helpapi.model.entity;

import com.mericakgul.helpapi.core.util.DeletedDateUtil;
import com.mericakgul.helpapi.enums.AssignmentStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "assignment")
@Getter
@Setter
@Where(clause = "DELETED_DATE = '1970-01-01 00:00:00.000'") // For Soft deleting
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_user", referencedColumnName = "username")
    private User customerUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_provider_user", referencedColumnName = "username")
    private User serviceProviderUser;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "assignment_status")
    @Enumerated(EnumType.STRING)
    private AssignmentStatus assignmentStatus;

    @Column(name = "deleted_date")
    private Date deletedDate = DeletedDateUtil.getDefaultDeletedDate();

}
