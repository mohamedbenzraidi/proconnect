package com.mohamedbenzraidi.proconnect.profile;

import com.mohamedbenzraidi.proconnect.profile.dto.EducationDto;
import com.mohamedbenzraidi.proconnect.profile.dto.ExperienceDto;
import com.mohamedbenzraidi.proconnect.profile.dto.ProfileDto;
import com.mohamedbenzraidi.proconnect.profile.dto.SkillDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/me")
    public ResponseEntity<ProfileDto> getMyProfile(Authentication authentication) {
        return ResponseEntity.ok(profileService.getProfileByEmail(authentication.getName()));
    }

    @PutMapping("/me")
    public ResponseEntity<ProfileDto> updateProfile(@RequestBody ProfileDto dto, Authentication authentication) {
        return ResponseEntity.ok(profileService.updateProfile(authentication.getName(), dto));
    }

    @PostMapping("/skills")
    public ResponseEntity<ProfileDto> addSkill(@RequestBody SkillDto skillDto, Authentication authentication) {
        return ResponseEntity.ok(profileService.addSkill(authentication.getName(), skillDto));
    }

    @PostMapping("/experiences")
    public ResponseEntity<ProfileDto> addExperience(@RequestBody ExperienceDto expDto, Authentication authentication) {
        return ResponseEntity.ok(profileService.addExperience(authentication.getName(), expDto));
    }
    @PostMapping("/education")
    public ResponseEntity<ProfileDto> addEducation(@RequestBody EducationDto eduDto, Authentication authentication) {
        return ResponseEntity.ok(profileService.addEducation(authentication.getName(),eduDto));
    }
}