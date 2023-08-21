package com.albert.microservice.authservice.controller;

import com.albert.microservice.authservice.entity.StaffCertifications;
import com.albert.microservice.authservice.request.OnboardingRequest;
import com.albert.microservice.authservice.request.StaffCertificationRequest;
import com.albert.microservice.authservice.service.StaffCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/certifications")
public class StaffCertificationController {
    @Autowired
    StaffCertificateService staffCertificateService;

    @PostMapping("/apply")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> applyCertification(@RequestBody StaffCertificationRequest staffCertificationRequest) {
        ResponseEntity<?> response = staffCertificateService.applyCertification(staffCertificationRequest);
        return response;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> viewAllCertification() {
        ResponseEntity<?> response = staffCertificateService.viewAllAppliedCertification();
        return response;
    }

    @GetMapping("/all/{userId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> viewAllMyCertification(@PathVariable int userId) {
        ResponseEntity<?> response = staffCertificateService.viewAllMyAppliedCertification(userId);
        return response;
    }

    @PutMapping("/{Id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> updateCertification(@RequestBody StaffCertificationRequest staffCertificationRequest,
                                                 @PathVariable int Id) {
        ResponseEntity<?> response = staffCertificateService.updateCertification(staffCertificationRequest, Id);
        return response;
    }

    @PutMapping("/approve/{Id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> approveCertification(@RequestBody StaffCertificationRequest staffCertificationRequest,
                                                  @PathVariable int Id) {
        ResponseEntity<?> response = staffCertificateService.approveCertification(staffCertificationRequest, Id);
        return response;
    }

    @PutMapping("/reject/{Id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> rejectCertification(@RequestBody StaffCertificationRequest staffCertificationRequest,
                                                 @PathVariable int Id) {
        ResponseEntity<?> response = staffCertificateService.rejectCertification(staffCertificationRequest, Id);
        return response;
    }
}
