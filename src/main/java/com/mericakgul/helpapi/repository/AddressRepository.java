package com.mericakgul.helpapi.repository;

import com.mericakgul.helpapi.model.entity.Address;
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
    // We could have used this method -> List<Address> findAddressByUser_UserUuid(UUID userUuid);
    // Just for practice purposes the one above is kept.

    List<Address> findAddressesByHouseNumberAndZipCodeAndCityAndCountry
            (Integer houseNumber, String zipCode, String city, String Country);
}
