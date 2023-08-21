package com.albert.microservice.authservice.service;

import com.albert.microservice.authservice.request.OnboardingRequest;
import com.albert.microservice.authservice.request.StaffCertificationRequest;
import org.springframework.http.ResponseEntity;

public interface StaffCertificateService {
    ResponseEntity<?> applyCertification(StaffCertificationRequest staffCertificationRequest);
    ResponseEntity<?> viewAllAppliedCertification();
    ResponseEntity<?> viewAllMyAppliedCertification(int userId);
    ResponseEntity<?> updateCertification(StaffCertificationRequest staffCertificationRequest,
                                          int id);
    ResponseEntity<?> approveCertification(StaffCertificationRequest staffCertificationRequest,
                                          int id);
    ResponseEntity<?> rejectCertification(StaffCertificationRequest staffCertificationRequest,
                                           int id);


}
