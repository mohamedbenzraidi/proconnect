package com.mohamedbenzraidi.proconnect.profile;

import com.mohamedbenzraidi.proconnect.profile.dto.EducationDto;
import com.mohamedbenzraidi.proconnect.profile.dto.ExperienceDto;
import com.mohamedbenzraidi.proconnect.profile.dto.SkillDto;
import com.mohamedbenzraidi.proconnect.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "profiles")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Skills {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String headline;

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String location;
    private String avatarUrl;

    // Stockage au format JSON dans PostgreSQL/SQL
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Builder.Default
    private List<SkillDto> skills = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Builder.Default
    private List<ExperienceDto> experiences = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Builder.Default
    private List<EducationDto> education = new ArrayList<>();
}