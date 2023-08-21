package com.albert.microservice.authservice.repository;

import com.albert.microservice.authservice.entity.Onboarding;;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OnboardingRepository extends JpaRepository<Onboarding, Long> {
    boolean existsByStaffNumberOrUserId(String staffNumber, Long userId);


    Optional<Onboarding> findByUserId(Long userId);
}

