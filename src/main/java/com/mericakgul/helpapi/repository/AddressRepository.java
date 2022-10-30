package com.mericakgul.helpapi.repository;

import com.mericakgul.helpapi.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
}
