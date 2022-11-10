package com.mericakgul.helpapi.repository;

import com.mericakgul.helpapi.model.entity.BusyPeriod;
import com.mericakgul.helpapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BusyPeriodRepository extends JpaRepository<BusyPeriod, Long> {

//    @Query(value = "SELECT BP FROM BusyPeriod BP WHERE BP.startDate=:startDate AND BP.endDate=:endDate")
    Optional<BusyPeriod> findBusyPeriodByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);

    @Query("SELECT BP.users FROM BusyPeriod BP WHERE BP.id=:id")
    List<User> findUsersByBusyPeriodId(@Param("id") Long id);

}
