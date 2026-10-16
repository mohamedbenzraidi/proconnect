package com.mohamedbenzraidi.proconnect.messaging;

import com.mohamedbenzraidi.proconnect.common.exception.ApiException;
import com.mohamedbenzraidi.proconnect.messaging.dto.ConversationDto;
import com.mohamedbenzraidi.proconnect.messaging.dto.MessageDto;
import com.mohamedbenzraidi.proconnect.messaging.dto.StartConversationRequest;
import com.mohamedbenzraidi.proconnect.messaging.dto.SendMessageRequest;
import com.mohamedbenzraidi.proconnect.user.User;
import com.mohamedbenzraidi.proconnect.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageService(
            ConversationRepository conversationRepository,
            MessageRepository messageRepository,
            UserRepository userRepository
    ) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<ConversationDto> getConversations(String currentUserEmail) {
        User currentUser = getUserByEmail(currentUserEmail);
        return conversationRepository.findByParticipantOneOrParticipantTwo(currentUser, currentUser)
                .stream()
                .map(conversation -> {
                    Message lastMessage = messageRepository
                            .findFirstByConversationOrderByCreatedAtDesc(conversation)
                            .orElse(null);
                    long unreadCount = messageRepository
                            .countByConversationAndRecipientAndReadAtIsNull(conversation, currentUser);
                    return ConversationDto.fromEntity(
                            conversation,
                            currentUser.getId(),
                            lastMessage == null ? null : lastMessage.getContent(),
                            lastMessage == null ? null : lastMessage.getCreatedAt(),
                            unreadCount
                    );
                })
                .sorted(Comparator.comparing(
                        ConversationDto::lastMessageAt,
                        Comparator.nullsLast(Comparator.reverseOrder())
                ))
                .toList();
    }

    @Transactional
    public List<MessageDto> getMessages(UUID conversationId, String currentUserEmail) {
        User currentUser = getUserByEmail(currentUserEmail);
        Conversation conversation = getConversationForUser(conversationId, currentUser);
        List<Message> messages = messageRepository.findByConversationOrderByCreatedAtAsc(conversation);
        messages.stream()
                .filter(message -> message.getRecipient().getId().equals(currentUser.getId())
                        && message.getReadAt() == null)
                .forEach(Message::markRead);
        return messageRepository.saveAll(messages).stream()
                .map(MessageDto::fromEntity)
                .toList();
    }

    @Transactional
    public MessageDto sendMessage(SendMessageRequest request, String currentUserEmail) {
        User sender = getUserByEmail(currentUserEmail);
        User recipient = getRecipient(request.recipientId(), sender);
        Conversation conversation = findOrCreateConversation(sender, recipient);
        Message message = messageRepository.save(new Message(conversation, sender, recipient, request.content()));
        return MessageDto.fromEntity(message);
    }

    @Transactional
    public ConversationDto startConversation(StartConversationRequest request, String currentUserEmail) {
        User sender = getUserByEmail(currentUserEmail);
        User recipient = getRecipient(request.recipientId(), sender);
        Conversation conversation = findOrCreateConversation(sender, recipient);
        Message lastMessage = messageRepository.findFirstByConversationOrderByCreatedAtDesc(conversation)
                .orElse(null);
        long unreadCount = messageRepository.countByConversationAndRecipientAndReadAtIsNull(conversation, sender);
        return ConversationDto.fromEntity(
                conversation,
                sender.getId(),
                lastMessage == null ? null : lastMessage.getContent(),
                lastMessage == null ? null : lastMessage.getCreatedAt(),
                unreadCount
        );
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
    }

    private User getRecipient(UUID recipientId, User sender) {
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Destinataire introuvable"));
        if (sender.getId().equals(recipient.getId())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Vous ne pouvez pas démarrer une conversation avec vous-même");
        }
        return recipient;
    }

    private Conversation findOrCreateConversation(User userOne, User userTwo) {
        User participantOne = userOne.getId().compareTo(userTwo.getId()) < 0 ? userOne : userTwo;
        User participantTwo = participantOne.getId().equals(userOne.getId()) ? userTwo : userOne;
        return conversationRepository.findByParticipantOneAndParticipantTwo(participantOne, participantTwo)
                .orElseGet(() -> conversationRepository.save(new Conversation(participantOne, participantTwo)));
    }

    private Conversation getConversationForUser(UUID conversationId, User user) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Conversation introuvable"));
        if (!conversation.contains(user)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Vous n'avez pas accès à cette conversation");
        }
        return conversation;
    }
}
