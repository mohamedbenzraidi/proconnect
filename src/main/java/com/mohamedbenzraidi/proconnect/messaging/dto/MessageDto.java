package com.mohamedbenzraidi.proconnect.messaging.dto;

import com.mohamedbenzraidi.proconnect.messaging.Message;

import java.time.Instant;
import java.util.UUID;

public record MessageDto(
        UUID id,
        UUID conversationId,
        UUID senderId,
        UUID recipientId,
        String content,
        Instant createdAt,
        boolean read
) {
    public static MessageDto fromEntity(Message message) {
        return new MessageDto(
                message.getId(),
                message.getConversation().getId(),
                message.getSender().getId(),
                message.getRecipient().getId(),
                message.getContent(),
                message.getCreatedAt(),
                message.getReadAt() != null
        );
    }
}
