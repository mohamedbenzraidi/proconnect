package com.mohamedbenzraidi.proconnect.connection;

import com.mohamedbenzraidi.proconnect.connection.dto.ConnectionDto;
import com.mohamedbenzraidi.proconnect.connection.dto.UserSearchDto;

import java.util.List;
import java.util.UUID;

public interface ConnectionService {
    List<UserSearchDto> searchUsers(String query, String currentUserEmail);
    List<ConnectionDto> getConnectionsForUser(String userEmail);
    ConnectionDto sendRequest(String senderEmail, UUID receiverId);
    ConnectionDto acceptRequest(UUID connectionId);
    void removeConnection(UUID connectionId);
}