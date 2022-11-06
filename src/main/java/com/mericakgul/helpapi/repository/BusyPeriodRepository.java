package com.mericakgul.helpapi.repository;

import com.mericakgul.helpapi.model.entity.BusyPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusyPeriodRepository extends JpaRepository<BusyPeriod, Long> {

}
