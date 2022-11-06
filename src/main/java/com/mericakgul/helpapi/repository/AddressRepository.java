package com.mericakgul.helpapi.repository;

import com.mericakgul.helpapi.model.entity.Address;
import com.mericakgul.helpapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {

    @Query(value = "SELECT A FROM Address A WHERE A.user.userUuid=:userUuid")
    List<Address> findAddressesByUserUuid(@Param("userUuid") UUID userUuid);
}
