package com.mohamedbenzraidi.proconnect.connection;

import com.mohamedbenzraidi.proconnect.connection.dto.ConnectionDto;
import com.mohamedbenzraidi.proconnect.connection.dto.SendConnectionRequest;
import com.mohamedbenzraidi.proconnect.connection.dto.UserSearchDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class ConnectionController {

    private final ConnectionService connectionService;

    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    // 1. Recherche d'utilisateurs
    @GetMapping("/users/search")
    public ResponseEntity<List<UserSearchDto>> searchUsers(@RequestParam("query") String query, Principal principal) {
        return ResponseEntity.ok(connectionService.searchUsers(query, principal.getName()));
    }

    // 2. Lister les connexions et demandes
    @GetMapping("/connections")
    public ResponseEntity<List<ConnectionDto>> getConnections(Principal principal) {
        return ResponseEntity.ok(connectionService.getConnectionsForUser(principal.getName()));
    }

    // 3. Envoyer une demande de connexion
    @PostMapping("/connections/request")
    public ResponseEntity<ConnectionDto> sendRequest(@RequestBody SendConnectionRequest request, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(connectionService.sendRequest(principal.getName(), request.getReceiverId()));
    }

    // 4. Accepter une demande
    @PostMapping("/connections/{id}/accept")
    public ResponseEntity<ConnectionDto> acceptConnection(@PathVariable UUID id) {
        return ResponseEntity.ok(connectionService.acceptRequest(id));
    }

    // 5. Supprimer / Refuser une connexion
    @DeleteMapping("/connections/{id}")
    public ResponseEntity<Void> removeConnection(@PathVariable UUID id) {
        connectionService.removeConnection(id);
        return ResponseEntity.noContent().build();
    }
}