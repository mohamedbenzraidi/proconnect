package com.mohamedbenzraidi.proconnect.opportunity.dto;

import com.mohamedbenzraidi.proconnect.opportunity.Opportunity;

import java.time.Instant;
import java.util.UUID;

public record OpportunityDto(
        UUID id,
        String title,
        String organization,
        String description,
        String location,
        String employmentType,
        String requiredSkills,
        Instant createdAt
) {
    public static OpportunityDto fromEntity(Opportunity entity) {
        return new OpportunityDto(
                entity.getId(),
                entity.getTitle(),
                entity.getOrganization(),
                entity.getDescription(),
                entity.getLocation(),
                entity.getEmploymentType(),
                entity.getRequiredSkills(),
                entity.getCreatedAt()
        );
    }
}