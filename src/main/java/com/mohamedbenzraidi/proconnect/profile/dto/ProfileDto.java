package com.mohamedbenzraidi.proconnect.profile.dto;

import com.mohamedbenzraidi.proconnect.user.Role;

import java.util.List;

public record ProfileDto(
        String id,
        String userId,
        String firstName,
        String lastName,
        String email,
        Role role,
        String headline,
        String bio,
        String location,
        String avatarUrl,
        List<SkillDto> skills,
        List<ExperienceDto> experiences,
        List<EducationDto> education
) {
    public ProfileDto {
        if (skills == null) skills = List.of();
        if (experiences == null) experiences = List.of();
        if (education == null) education = List.of();
    }
}