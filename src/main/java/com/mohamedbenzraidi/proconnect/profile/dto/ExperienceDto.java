package com.mohamedbenzraidi.proconnect.profile.dto;

public record ExperienceDto(
        String id,
        String company,
        String position,
        String startDate,
        String endDate,
        Boolean isCurrent,
        String description
) {
    public ExperienceDto {
        if (isCurrent == null) isCurrent = false;
    }
}