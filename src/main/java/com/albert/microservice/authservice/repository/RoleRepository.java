package com.albert.microservice.authservice.repository;

import com.albert.microservice.authservice.entity.ERole;
import com.albert.microservice.authservice.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
