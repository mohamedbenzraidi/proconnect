package com.mohamedbenzraidi.proconnect.messaging;

import com.mohamedbenzraidi.proconnect.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {
    List<Conversation> findByParticipantOneOrParticipantTwo(User participantOne, User participantTwo);

    Optional<Conversation> findByParticipantOneAndParticipantTwo(User participantOne, User participantTwo);
}
