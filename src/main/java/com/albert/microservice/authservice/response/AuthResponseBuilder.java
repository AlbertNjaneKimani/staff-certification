package com.albert.microservice.authservice.response;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public class AuthResponseBuilder {
    public static AuthResponse buildResponse(String requestRefId,int statusCode, HttpStatus responseMessage, String customerMessage, Instant timestamp,Object payload) {
        AuthResponse.Header header = new AuthResponse.Header();
        header.setRequestRefId(UUID.randomUUID().toString());
        header.setResponseCode(statusCode);
        header.setResponseMessage(responseMessage);
        header.setCustomerMessage(customerMessage);
        header.setTimestamp(Instant.now());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setHeader(header);
        authResponse.setBody(payload);

        return authResponse;
    }
}
