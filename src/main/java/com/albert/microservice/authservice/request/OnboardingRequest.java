package com.albert.microservice.authservice.request;



import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
public class OnboardingRequest {
    @NotNull(message = "User ID is required")
    @Max(value = 20, message = "User ID should not exceed 20 characters")
    private Long userId;

    @NotBlank(message = "First name is required")
    @Size(max = 30, message = "First name should not exceed 30 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 30, message = "Last name should not exceed 30 characters")
    private String lastName;

    @NotBlank(message = "Staff number is required")
    @Size(max = 10, message = "Staff number should not exceed 10 characters")
    private String staffNumber;

    @NotBlank(message = "Department is required")
    @Size(max = 50, message = "Department should not exceed 50 characters")
    private String department;

    @NotBlank(message = "Division is required")
    @Size(max = 50, message = "Division should not exceed 50 characters")
    private String division;

    @NotBlank(message = "One more skill is required")
    @Size(max = 50, message = "One more skill should not exceed 50 characters")
    private String oneMoreSkill;
}
