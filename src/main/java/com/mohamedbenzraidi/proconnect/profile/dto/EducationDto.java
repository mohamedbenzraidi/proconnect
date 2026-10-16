package com.mohamedbenzraidi.proconnect.profile.dto;

public record EducationDto(
        String id,
        String institution,
        String degree,
        String fieldOfStudy,
        String startDate,
        String endDate
) {}