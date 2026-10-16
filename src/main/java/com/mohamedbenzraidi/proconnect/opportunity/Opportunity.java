package com.mohamedbenzraidi.proconnect.opportunity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "opportunities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Opportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String organization;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(name = "employment_type", nullable = false)
    private String employmentType; // STAGE, CDI, FREELANCE

    @Column(name = "required_skills")
    private String requiredSkills;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Opportunity(String title, String organization, String description, String location, String employmentType, String requiredSkills) {
        this.title = title;
        this.organization = organization;
        this.description = description;
        this.location = location;
        this.employmentType = employmentType;
        this.requiredSkills = requiredSkills;
        this.createdAt = Instant.now();
    }
}