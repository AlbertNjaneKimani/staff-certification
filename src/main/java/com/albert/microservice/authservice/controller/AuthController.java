package com.albert.microservice.authservice.controller;

import com.albert.microservice.authservice.request.ChangePasswordRequest;
import com.albert.microservice.authservice.request.LoginRequest;
import com.albert.microservice.authservice.request.SignupRequest;
import com.albert.microservice.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }

    @PutMapping("/{userId}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> disableUser(@PathVariable("userId") long userId) {
        return authService.disableUser(userId);
    }
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUsers() {
        return authService.getUser();
    }

    @PutMapping("/{userId}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> enableUser(@PathVariable("userId") long userId) {
        return authService.enableUser(userId);
    }

    @PutMapping("change/password/{userId}")
    public ResponseEntity<?> changePassword(@PathVariable("userId") long userId, @RequestBody ChangePasswordRequest passwordRequest) {
        return authService.changePassword(userId, passwordRequest);
    }

}
