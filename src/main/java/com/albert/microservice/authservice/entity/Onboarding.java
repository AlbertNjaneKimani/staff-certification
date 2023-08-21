package com.albert.microservice.authservice.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "onboarding")
public class Onboarding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "staff_number")
    private String staffNumber;

    private String department;

    private String division;

    @Column(name = "one_more_skill")
    private String oneMoreSkill;

    public Onboarding() {
    }
    public Onboarding(Long userId, String firstName, String lastName, String staffNumber, String department, String division, String oneMoreSkill) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.staffNumber = staffNumber;
        this.department = department;
        this.division = division;
        this.oneMoreSkill = oneMoreSkill;
    }
}
