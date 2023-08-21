package com.albert.microservice.authservice.service;

import com.albert.microservice.authservice.entity.ERole;
import com.albert.microservice.authservice.entity.Role;
import com.albert.microservice.authservice.entity.User;
import com.albert.microservice.authservice.jwt.JwtUtils;
import com.albert.microservice.authservice.repository.RoleRepository;
import com.albert.microservice.authservice.repository.UserRepository;
import com.albert.microservice.authservice.request.ChangePasswordRequest;
import com.albert.microservice.authservice.request.LoginRequest;
import com.albert.microservice.authservice.request.SignupRequest;
import com.albert.microservice.authservice.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(), HttpStatus.OK.value(),
                HttpStatus.OK,
                "Success", Instant.now(), new JwtResponse(jwt));
        return ResponseEntity.ok(authResponse);
    }

    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(), HttpStatus.CONFLICT.value(),
                    HttpStatus.CONFLICT,
                    "Username is already taken", Instant.now(), new MessageResponse(null));
            return ResponseEntity
                    .badRequest()
                    .body(authResponse);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(), HttpStatus.CONFLICT.value(),
                    HttpStatus.CONFLICT,
                    "Email is already Taken", Instant.now(), new MessageResponse(null));
            return ResponseEntity
                    .badRequest()
                    .body(authResponse);
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "ROLE_ADMIN":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "ROLE_MANAGER":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MANAGER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        user.setIsDisabled(0);
        userRepository.save(user);
        AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(), HttpStatus.OK.value(),
                HttpStatus.OK,
                "Success", Instant.now(), new MessageResponse("User registered successfully!"));

        return ResponseEntity.ok(authResponse);
    }

    public ResponseEntity<?> disableUser(long userId) {
        return updateUserStatus(userId, 1);
    }

    public ResponseEntity<?> enableUser(long userId) {
        return updateUserStatus(userId, 0);
    }

    private ResponseEntity<?> updateUserStatus(long userId, int disable) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            user.setIsDisabled(disable);
            userRepository.save(user);

            String message = disable == 0 ? "User enabled successfully!" : "User disabled successfully!";
            AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(), HttpStatus.OK.value(),
                    HttpStatus.OK,
                    "Success", Instant.now(), new MessageResponse(message));

            return ResponseEntity.ok(authResponse);
        } else {
            AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(), HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND,
                    "User not found", Instant.now(), new MessageResponse(null));

            return ResponseEntity.ok(authResponse);
        }
    }

    public ResponseEntity<?> changePassword(long userId, ChangePasswordRequest passwordRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        if (!encoder.matches(passwordRequest.getCurrentPassword(), user.getPassword())) {
            AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(), HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST,
                    "Current password is incorrect", Instant.now(), new MessageResponse(null));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authResponse);
        }
        if (!passwordRequest.getNewPassword().equalsIgnoreCase(passwordRequest.getConfirmPassword())) {
            AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(), HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST,
                    "Password do not match", Instant.now(), new MessageResponse(null));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(authResponse);
        }
        String newPassword = encoder.encode(passwordRequest.getNewPassword());
        user.setPassword(newPassword);
        userRepository.save(user);

        AuthResponse authResponse = AuthResponseBuilder.buildResponse(UUID.randomUUID().toString(), HttpStatus.OK.value(),
                HttpStatus.OK,
                "Password changed successfully", Instant.now(), new MessageResponse("Password changed successfully"));
        return ResponseEntity.ok(authResponse);
    }

    private Set<String> mapRolesToStringSet(Set<ERole> roles) {
        return roles.stream()
                .map(ERole::name) // Convert ERole to its name (String)
                .collect(Collectors.toSet());
    }

    public ResponseEntity<?> getUser() {

        List<UserResponse> userResponses = userRepository.findAll().stream()
                .map(res -> {
                    UserResponse userResponse = new UserResponse();
                    userResponse.setId(res.getId());
                    userResponse.setEmail(res.getEmail());
                    userResponse.setUsername(res.getUsername());
                    userResponse.setIsDisabled(res.getIsDisabled());
                    userResponse.setRoles(res.getRoles());
                    return userResponse; // Return the mapped UserResponse
                })
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(userResponses);


    }
}

