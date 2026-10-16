package com.mohamedbenzraidi.proconnect.messaging.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record SendMessageRequest(
        @NotNull UUID recipientId,
        @NotBlank @Size(max = 5000) String content
) {}
