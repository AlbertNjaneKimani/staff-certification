package com.albert.microservice.authservice.repository;

import com.albert.microservice.authservice.entity.StaffCertifications;
import com.fasterxml.jackson.annotation.OptBoolean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StaffCertificationRepository extends JpaRepository<StaffCertifications, Integer> {
    boolean existsByCertificationNameAndUserId(String name, int userid);

    List<StaffCertifications> getStaffCertificationsByUserId(int id);

    int  countStaffCertificationsByUserIdAndCertificatePeriod(int userId, String period);

    StaffCertifications getStaffCertificationsById(int id);
}
