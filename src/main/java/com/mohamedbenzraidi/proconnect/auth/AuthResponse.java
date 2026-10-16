package com.mohamedbenzraidi.proconnect.auth;

public record AuthResponse(
        String token,
        String userId,
        String email,
        String firstName,
        String lastName
) {}