package com.albert.microservice.authservice.service.impl;

import com.albert.microservice.authservice.entity.Onboarding;
import com.albert.microservice.authservice.repository.OnboardingRepository;
import com.albert.microservice.authservice.request.OnboardingRequest;
import com.albert.microservice.authservice.request.UpdateOnboardingRequest;
import com.albert.microservice.authservice.response.AuthResponse;
import com.albert.microservice.authservice.response.AuthResponseBuilder;
import com.albert.microservice.authservice.response.MessageResponse;
import com.albert.microservice.authservice.service.OnboardingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OnboardingServiceImpl implements OnboardingService {

    private static final Logger logger = LoggerFactory.getLogger(OnboardingServiceImpl.class);

    @Autowired
    OnboardingRepository onboardingRepository;

    @Override
    public ResponseEntity<?> addOnboardingDetails(OnboardingRequest onboardingRequest) {
        logger.info("Adding onboarding details for user: {}", onboardingRequest.getUserId());

        boolean staffNumberExists = onboardingRepository.existsByStaffNumberOrUserId(onboardingRequest.getStaffNumber(),
                onboardingRequest.getUserId());
        if (staffNumberExists) {
            logger.warn("Staff record already exists: {}", onboardingRequest.getStaffNumber());

            AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                    HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, "Staff number already exists",
                    Instant.now(), new MessageResponse(null));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authResponse);
        }

        Onboarding onboarding = new Onboarding();
        onboarding.setUserId(onboardingRequest.getUserId());
        onboarding.setFirstName(onboardingRequest.getFirstName());
        onboarding.setLastName(onboardingRequest.getLastName());
        onboarding.setStaffNumber(onboardingRequest.getStaffNumber());
        onboarding.setDepartment(onboardingRequest.getDepartment());
        onboarding.setDivision(onboardingRequest.getDivision());
        onboarding.setOneMoreSkill(onboardingRequest.getOneMoreSkill());
        onboardingRepository.save(onboarding);

        logger.info("Onboarding details added successfully for user: {}", onboardingRequest.getUserId());

        AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                HttpStatus.CREATED.value(), HttpStatus.CREATED, "Onboarding details added successfully",
                Instant.now(), new MessageResponse("Onboarding details added successfully"));

        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }

    @Override
    public ResponseEntity<?> getOnboardingDetailsByUserId(Long userId) {
        logger.info("Fetching onboarding details for userId: {}", userId);

        Optional<Onboarding> onboardingOptional = onboardingRepository.findByUserId(userId);
        if (onboardingOptional.isPresent()) {
            Onboarding onboarding = onboardingOptional.get();
            logger.info("Fetched onboarding details for userId: {}", userId);
            AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                    HttpStatus.OK.value(), HttpStatus.OK, "Request processed successfully",
                    Instant.now(), onboarding);
            return ResponseEntity.ok(authResponse);
        } else {
            logger.warn("No onboarding details found for userId: {}", userId);
            AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                    HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, "No onboarding details found",
                    Instant.now(), new MessageResponse(null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(authResponse);
        }
    }

    @Override
    public ResponseEntity<?> getOnboardingDetails() {
        logger.info("Fetching onboarding details for userId");

        List<Onboarding> onboardingOptional = onboardingRepository.findAll();
        if (onboardingOptional.size() > 0) {
            logger.info("Fetched onboarding details for userId");
            AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                    HttpStatus.OK.value(), HttpStatus.OK, "Request processed successfully",
                    Instant.now(), onboardingOptional);
            return ResponseEntity.ok(authResponse);
        } else {
            logger.warn("No records found");
            AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                    HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, "No onboarding details found",
                    Instant.now(), new MessageResponse(null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(authResponse);
        }
    }

    @Override
    public ResponseEntity<?> updateOnboardingDetailsByUserId(Long userId, UpdateOnboardingRequest onboardingRequest) {
        logger.info("Updating onboarding details for user: {}", userId);

        Optional<Onboarding> existingOnboarding = onboardingRepository.findByUserId(userId);
        if (existingOnboarding.isEmpty()) {
            logger.warn("Onboarding details not found for user: {}", userId);

            AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                    HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, "Onboarding details not found",
                    Instant.now(), new MessageResponse(null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(authResponse);
        }

        Onboarding onboarding = existingOnboarding.get();
        onboarding.setDepartment(onboardingRequest.getDepartment());
        onboarding.setDivision(onboardingRequest.getDivision());
        onboarding.setOneMoreSkill(onboardingRequest.getOneMoreSkill());
        onboardingRepository.save(onboarding);

        logger.info("Onboarding details updated successfully for user: {}", userId);

        AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(),
                HttpStatus.OK.value(), HttpStatus.OK, "Onboarding details updated successfully",
                Instant.now(), new MessageResponse("Onboarding details updated successfully"));

        return ResponseEntity.status(HttpStatus.OK).body(authResponse);
    }
    @Override
    public ResponseEntity<?> deleteOnboardingDetailsById(Long id) {
        if (!onboardingRepository.existsById(id)) {
            AuthResponse authResponse = AuthResponseBuilder.buildResponse(
                    UUID.randomUUID().toString(),
                    HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND,
                    "Onboarding details not found",
                    Instant.now(),
                    new MessageResponse(null));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(authResponse);
        }

        onboardingRepository.deleteById(id);

        AuthResponse authResponse = AuthResponseBuilder.buildResponse(
                UUID.randomUUID().toString(),
                HttpStatus.OK.value(),
                HttpStatus.OK,
                "Onboarding details deleted successfully",
                Instant.now(),
                new MessageResponse("Onboarding details deleted successfully"));
        return ResponseEntity.ok(authResponse);
    }
}
