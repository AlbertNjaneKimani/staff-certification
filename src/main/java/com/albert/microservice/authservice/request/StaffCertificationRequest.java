package com.albert.microservice.authservice.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StaffCertificationRequest {
    private int userId;
    private String certificationName;
    private String provider;
    private String certificatePeriod;
    private LocalDate certificateDate;
    private String status;
    private String comments;
}
