package com.mericakgul.helpapi.repository;

import com.mericakgul.helpapi.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
