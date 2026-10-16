package com.mohamedbenzraidi.proconnect.messaging.dto;

import com.mohamedbenzraidi.proconnect.messaging.Conversation;

import java.time.Instant;
import java.util.UUID;

public record ConversationDto(
        UUID id,
        UUID userId,
        String firstName,
        String lastName,
        String email,
        String lastMessage,
        Instant lastMessageAt,
        long unreadCount
) {
    public static ConversationDto fromEntity(
            Conversation conversation,
            UUID currentUserId,
            String lastMessage,
            Instant lastMessageAt,
            long unreadCount
    ) {
        var other = conversation.getParticipantOne().getId().equals(currentUserId)
                ? conversation.getParticipantTwo()
                : conversation.getParticipantOne();
        return new ConversationDto(
                conversation.getId(),
                other.getId(),
                other.getFirstName(),
                other.getLastName(),
                other.getEmail(),
                lastMessage,
                lastMessageAt,
                unreadCount
        );
    }
}
