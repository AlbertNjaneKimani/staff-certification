package com.albert.microservice.authservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity(name = "staff_certifications")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StaffCertifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    private String certificationName;
    private String provider;
    private String certificatePeriod;
    private LocalDate certificateDate;
    private String status;
    private String comments;
}
