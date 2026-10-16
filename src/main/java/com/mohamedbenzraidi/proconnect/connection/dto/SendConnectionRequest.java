package com.mohamedbenzraidi.proconnect.connection.dto;

import java.util.UUID;

public class SendConnectionRequest {
    private UUID receiverId;

    public SendConnectionRequest() {}

    public SendConnectionRequest(UUID receiverId) {
        this.receiverId = receiverId;
    }

    public UUID getReceiverId() { return receiverId; }
    public void setReceiverId(UUID receiverId) { this.receiverId = receiverId; }
}