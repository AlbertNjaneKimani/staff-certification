package com.albert.microservice.authservice.service.impl;

import com.albert.microservice.authservice.entity.Onboarding;
import com.albert.microservice.authservice.entity.StaffCertifications;
import com.albert.microservice.authservice.repository.OnboardingRepository;
import com.albert.microservice.authservice.repository.StaffCertificationRepository;
import com.albert.microservice.authservice.request.StaffCertificationRequest;
import com.albert.microservice.authservice.response.AuthResponse;
import com.albert.microservice.authservice.response.AuthResponseBuilder;
import com.albert.microservice.authservice.response.MessageResponse;
import com.albert.microservice.authservice.service.StaffCertificateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StaffCertificateServiceImpl implements StaffCertificateService {
    private static final Logger logger = LoggerFactory.getLogger(OnboardingServiceImpl.class);

    @Autowired
    StaffCertificationRepository staffCertificationRepository;

    @Value("${app.maximum.certifications}")
    private int maximumCertifications;

    @Override
    public ResponseEntity<?> applyCertification(StaffCertificationRequest staffCertificationRequest) {
        int certificationsApplied = staffCertificationRepository.countStaffCertificationsByUserIdAndCertificatePeriod(staffCertificationRequest.getUserId(),
                staffCertificationRequest.getCertificatePeriod());
        if (certificationsApplied > maximumCertifications) {
            AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                    HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, "You Have reached maximum allowed Certifications",
                    Instant.now(), new MessageResponse(null));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authResponse);
        }
        boolean certName = staffCertificationRepository.existsByCertificationNameAndUserId(staffCertificationRequest.getCertificationName(),
                staffCertificationRequest.getUserId());
        if (certName) {
            AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                    HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, "You Have Applied for similar certification",
                    Instant.now(), new MessageResponse(null));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authResponse);
        }

        StaffCertifications certifications = new StaffCertifications();
        certifications.setCertificationName(staffCertificationRequest.getCertificationName());
        certifications.setCertificateDate(LocalDate.now());
        certifications.setCertificatePeriod(staffCertificationRequest.getCertificatePeriod());
        certifications.setComments(staffCertificationRequest.getComments());
        certifications.setStatus("Pending");
        certifications.setProvider(staffCertificationRequest.getProvider());
        certifications.setUserId(staffCertificationRequest.getUserId());
        staffCertificationRepository.save(certifications);
        AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                HttpStatus.CREATED.value(), HttpStatus.CREATED, "Applied for Certification Successfully",
                Instant.now(), new MessageResponse("Applied for Certification Successfully"));

        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @Override
    public ResponseEntity<?> viewAllAppliedCertification() {
        List<StaffCertifications> staffCertifications = staffCertificationRepository.findAll();
        if (staffCertifications.size() < 0) {
            AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                    HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, "No records",
                    Instant.now(), new MessageResponse("No Records"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(authResponse);
        }
        AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                HttpStatus.OK.value(), HttpStatus.OK, "Operation completed successfully",
                Instant.now(), staffCertifications);
        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }

    @Override
    public ResponseEntity<?> viewAllMyAppliedCertification(int userId) {
        List<StaffCertifications> staffCertifications = staffCertificationRepository.getStaffCertificationsByUserId(userId);
        if (staffCertifications.size() < 0 || staffCertifications == null) {
            AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                    HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, "No records found",
                    Instant.now(), new MessageResponse("No Records found"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(authResponse);
        }
        AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                HttpStatus.OK.value(), HttpStatus.OK, "Operation completed successfully",
                Instant.now(), staffCertifications);
        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }

    @Override
    public ResponseEntity<?> updateCertification(StaffCertificationRequest staffCertificationRequest, int id) {
        AuthResponse authResponse;
        StaffCertifications staffCertifications = staffCertificationRepository.getStaffCertificationsById(id);
        if (staffCertifications == null) {
            authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                    HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, "No records found",
                    Instant.now(), new MessageResponse("No Records found"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(authResponse);
        } else if (staffCertifications.getStatus().equalsIgnoreCase("Approved")
                || staffCertifications.getStatus().equalsIgnoreCase("Rejected")
                || staffCertifications.getStatus().equalsIgnoreCase("Completed")) {
            authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                    HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, "Approval not allowed",
                    Instant.now(), new MessageResponse("Approval not allowed"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authResponse);
        } else {

            staffCertifications.setCertificationName(staffCertificationRequest.getCertificationName());
            staffCertifications.setProvider(staffCertificationRequest.getProvider());
            staffCertifications.setCertificateDate(staffCertificationRequest.getCertificateDate());
            staffCertificationRepository.save(staffCertifications);
            authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                    HttpStatus.OK.value(), HttpStatus.OK, "Record updated successfully",
                    Instant.now(), new MessageResponse("Record updated successfully"));
            return ResponseEntity.status(HttpStatus.OK).body(authResponse);
        }
    }

    @Override
    public ResponseEntity<?> approveCertification(StaffCertificationRequest staffCertificationRequest, int id) {
        AuthResponse authResponse;
        StaffCertifications staffCertifications = staffCertificationRepository.getStaffCertificationsById(id);
        if (staffCertifications == null) {
            authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                    HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, "No records found",
                    Instant.now(), new MessageResponse("No Records found"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(authResponse);
        } else {

            staffCertifications.setStatus("Approved");
            staffCertifications.setComments(staffCertificationRequest.getComments());
            staffCertificationRepository.save(staffCertifications);
            authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                    HttpStatus.OK.value(), HttpStatus.OK, "Record Approved successfully",
                    Instant.now(), new MessageResponse("Record Approved successfully"));
            return ResponseEntity.status(HttpStatus.OK).body(authResponse);
        }
    }

    @Override
    public ResponseEntity<?> rejectCertification(StaffCertificationRequest staffCertificationRequest, int id) {
        AuthResponse authResponse;
        StaffCertifications staffCertifications = staffCertificationRepository.getStaffCertificationsById(id);
        if (staffCertifications == null) {
            authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                    HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, "No records found",
                    Instant.now(), new MessageResponse("No Records found"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(authResponse);
        } else {

            staffCertifications.setStatus("Rejected");
            staffCertifications.setComments(staffCertificationRequest.getComments());
            staffCertificationRepository.save(staffCertifications);
            authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                    HttpStatus.OK.value(), HttpStatus.OK, "Record rejected successfully",
                    Instant.now(), new MessageResponse("Record rejected successfully"));
            return ResponseEntity.status(HttpStatus.OK).body(authResponse);
        }
    }
}
