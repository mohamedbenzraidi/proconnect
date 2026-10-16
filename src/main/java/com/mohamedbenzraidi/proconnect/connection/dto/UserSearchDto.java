package com.mohamedbenzraidi.proconnect.connection.dto;

import com.mohamedbenzraidi.proconnect.user.User;
import java.util.UUID;

public record UserSearchDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String role,
        String connectionStatus
) {
    public static UserSearchDto fromEntity(User user, String connectionStatus) {
        return new UserSearchDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().name(),
                connectionStatus
        );
    }
}