package com.mohamedbenzraidi.proconnect.messaging;

import com.mohamedbenzraidi.proconnect.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByConversationOrderByCreatedAtAsc(Conversation conversation);

    Optional<Message> findFirstByConversationOrderByCreatedAtDesc(Conversation conversation);

    long countByConversationAndRecipientAndReadAtIsNull(Conversation conversation, User recipient);
}
