package com.albert.microservice.authservice.controller;

import com.albert.microservice.authservice.request.OnboardingRequest;
import com.albert.microservice.authservice.request.UpdateOnboardingRequest;
import com.albert.microservice.authservice.service.OnboardingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/staffs")
public class OnboardingController {
    @Autowired
    private OnboardingService onboardingService;

    @PostMapping("/details")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> addOnboardingDetails(@RequestBody OnboardingRequest onboardingRequest) {
        ResponseEntity<?> response = onboardingService.addOnboardingDetails(onboardingRequest);
        return response;
    }

    @GetMapping("/details/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> getOnboardingDetailsByUserId(@PathVariable Long userId) {
        return onboardingService.getOnboardingDetailsByUserId(userId);
    }

    @GetMapping("/details")
    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    public ResponseEntity<?> getOnboardingDetails() {
        return onboardingService.getOnboardingDetails();
    }

    @PutMapping("/details/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateOnboardingDetailsByUserId(@PathVariable Long userId, @RequestBody UpdateOnboardingRequest onboardingRequest) {
        return onboardingService.updateOnboardingDetailsByUserId(userId, onboardingRequest);
    }

    @DeleteMapping("/details/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteOnboardingDetailsById(@PathVariable Long id) {
        return onboardingService.deleteOnboardingDetailsById(id);
    }

}

