package com.mohamedbenzraidi.proconnect.auth;

import com.mohamedbenzraidi.proconnect.user.Role;
import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank @Size(max = 100) String firstName,
        @NotBlank @Size(max = 100) String lastName,
        @NotBlank @Email String email,
        @NotNull Role role,
        @NotBlank @Size(min = 8, max = 72) String password
) {}