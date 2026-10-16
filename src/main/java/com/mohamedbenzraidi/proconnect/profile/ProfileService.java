package com.mohamedbenzraidi.proconnect.profile;

import com.mohamedbenzraidi.proconnect.profile.dto.EducationDto;
import com.mohamedbenzraidi.proconnect.user.User;
import com.mohamedbenzraidi.proconnect.profile.dto.ExperienceDto;
import com.mohamedbenzraidi.proconnect.profile.dto.ProfileDto;
import com.mohamedbenzraidi.proconnect.profile.dto.SkillDto;
import com.mohamedbenzraidi.proconnect.profile.dto.EducationDto;
import com.mohamedbenzraidi.proconnect.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ProfileDto getProfileByEmail(String email) {
        Profile profile = profileRepository.findByUserEmail(email)
                .orElseGet(() -> createInitialProfile(email));
        return mapToDto(profile);
    }

    @Transactional
    public ProfileDto updateProfile(String email, ProfileDto dto) {
        Profile profile = profileRepository.findByUserEmail(email)
                .orElseGet(() -> createInitialProfile(email));

        profile.setHeadline(dto.headline());
        profile.setBio(dto.bio());
        profile.setLocation(dto.location());
        profile.setAvatarUrl(dto.avatarUrl());

        return mapToDto(profileRepository.save(profile));
    }

    @Transactional
    public ProfileDto addSkill(String email, SkillDto skillDto) {
        Profile profile = profileRepository.findByUserEmail(email)
                .orElseGet(() -> createInitialProfile(email));

        // Générer un ID unique si le client ne l'a pas fourni
        SkillDto newSkill = new SkillDto(
                skillDto.id() != null ? skillDto.id() : UUID.randomUUID().toString(),
                skillDto.name()
        );

        profile.getSkills().add(newSkill);
        return mapToDto(profileRepository.save(profile));
    }

    @Transactional
    public ProfileDto addEducation(String email, EducationDto educationDto) {
        Profile profile = profileRepository.findByUserEmail(email)
                .orElseGet(() -> createInitialProfile(email));


        EducationDto newEducation = new EducationDto(
                educationDto.id() != null ? educationDto.id() : UUID.randomUUID().toString(),
                educationDto.institution(),
                educationDto.degree(),
                educationDto.fieldOfStudy(),
                educationDto.startDate(),
                educationDto.endDate()
        );

        profile.getEducation().add(newEducation);
        return mapToDto(profileRepository.save(profile));
    }

    @Transactional
    public ProfileDto addExperience(String email, ExperienceDto expDto) {
        Profile profile = profileRepository.findByUserEmail(email)
                .orElseGet(() -> createInitialProfile(email));

        ExperienceDto newExp = new ExperienceDto(
                expDto.id() != null ? expDto.id() : UUID.randomUUID().toString(),
                expDto.company(),
                expDto.position(),
                expDto.startDate(),
                expDto.endDate(),
                expDto.isCurrent(),
                expDto.description()
        );

        profile.getExperiences().add(newExp);
        return mapToDto(profileRepository.save(profile));
    }

    private Profile createInitialProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'email: " + email));

        Profile newProfile = Profile.builder()
                .user(user)
                .build();

        return profileRepository.save(newProfile);
    }

    private ProfileDto mapToDto(Profile profile) {
        User u = profile.getUser();
        return new ProfileDto(
                profile.getId().toString(),
                u.getId().toString(),
                u.getFirstName(),
                u.getLastName(),
                u.getEmail(),
                u.getRole(),
                profile.getHeadline(),
                profile.getBio(),
                profile.getLocation(),
                profile.getAvatarUrl(),
                profile.getSkills(),
                profile.getExperiences(),
                profile.getEducation()
        );
    }
}