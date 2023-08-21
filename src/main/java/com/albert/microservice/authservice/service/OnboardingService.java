package com.albert.microservice.authservice.service;

import com.albert.microservice.authservice.request.OnboardingRequest;
import com.albert.microservice.authservice.request.UpdateOnboardingRequest;
import org.springframework.http.ResponseEntity;

public interface OnboardingService {
    ResponseEntity<?> addOnboardingDetails(OnboardingRequest onboardingRequest);

    public ResponseEntity<?> getOnboardingDetailsByUserId(Long userId);

    public ResponseEntity<?> getOnboardingDetails();

    public ResponseEntity<?> updateOnboardingDetailsByUserId(Long userId, UpdateOnboardingRequest onboardingRequest);

    public ResponseEntity<?> deleteOnboardingDetailsById(Long id);
}