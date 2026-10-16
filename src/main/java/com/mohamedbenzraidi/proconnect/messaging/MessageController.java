package com.mohamedbenzraidi.proconnect.messaging;

import com.mohamedbenzraidi.proconnect.messaging.dto.ConversationDto;
import com.mohamedbenzraidi.proconnect.messaging.dto.MessageDto;
import com.mohamedbenzraidi.proconnect.messaging.dto.SendMessageRequest;
import com.mohamedbenzraidi.proconnect.messaging.dto.StartConversationRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationDto>> getConversations(Principal principal) {
        return ResponseEntity.ok(messageService.getConversations(principal.getName()));
    }

    @PostMapping("/conversations")
    public ResponseEntity<ConversationDto> startConversation(
            @Valid @RequestBody StartConversationRequest request,
            Principal principal
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageService.startConversation(request, principal.getName()));
    }

    @GetMapping("/conversations/{id}/messages")
    public ResponseEntity<List<MessageDto>> getMessages(
            @PathVariable UUID id,
            Principal principal
    ) {
        return ResponseEntity.ok(messageService.getMessages(id, principal.getName()));
    }

    @PostMapping("/messages")
    public ResponseEntity<MessageDto> sendMessage(
            @Valid @RequestBody SendMessageRequest request,
            Principal principal
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageService.sendMessage(request, principal.getName()));
    }
}
