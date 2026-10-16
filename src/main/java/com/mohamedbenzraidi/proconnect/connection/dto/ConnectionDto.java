package com.mohamedbenzraidi.proconnect.connection.dto;

import com.mohamedbenzraidi.proconnect.connection.Connection;
import com.mohamedbenzraidi.proconnect.connection.ConnectionStatus;

import java.time.Instant;
import java.util.UUID;

public record ConnectionDto(
        UUID id,
        UUID senderId,
        String senderName,
        UUID receiverId,
        String receiverName,
        ConnectionStatus status,
        Instant createdAt
) {
    public static ConnectionDto fromEntity(Connection connection) {
        return new ConnectionDto(
                connection.getId(),
                connection.getSender().getId(),
                connection.getSender().getFirstName() + " " + connection.getSender().getLastName(),
                connection.getReceiver().getId(),
                connection.getReceiver().getLastName() != null ?
                        connection.getReceiver().getFirstName() + " " + connection.getReceiver().getLastName() :
                        connection.getReceiver().getFirstName(),
                connection.getStatus(),
                connection.getCreatedAt()
        );
    }
}