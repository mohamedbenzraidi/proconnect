package com.mohamedbenzraidi.proconnect.opportunity.dto;

public record CreateOpportunityRequest(
        String title,
        String organization,
        String description,
        String location,
        String employmentType,
        String requiredSkills
) {}