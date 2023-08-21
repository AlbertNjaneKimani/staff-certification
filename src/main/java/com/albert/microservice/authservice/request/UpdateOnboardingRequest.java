package com.albert.microservice.authservice.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
public class UpdateOnboardingRequest {
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
