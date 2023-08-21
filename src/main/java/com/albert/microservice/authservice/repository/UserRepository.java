package com.albert.microservice.authservice.repository;

import com.albert.microservice.authservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndIsDisabled(String username,int isDisabled);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
