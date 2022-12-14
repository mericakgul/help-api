package com.mericakgul.helpapi.repository;

import com.mericakgul.helpapi.model.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findAssignmentByCustomerUser_Username(String username);
    List<Assignment> findAssignmentByServiceProviderUser_Username(String username);
}
