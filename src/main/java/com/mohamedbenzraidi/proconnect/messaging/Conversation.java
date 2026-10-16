package com.mohamedbenzraidi.proconnect.messaging;

import com.mohamedbenzraidi.proconnect.user.User;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "conversations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"participant_one_id", "participant_two_id"})
})
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "participant_one_id", nullable = false)
    private User participantOne;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "participant_two_id", nullable = false)
    private User participantTwo;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    protected Conversation() {}

    public Conversation(User participantOne, User participantTwo) {
        this.participantOne = participantOne;
        this.participantTwo = participantTwo;
    }

    public UUID getId() { return id; }
    public User getParticipantOne() { return participantOne; }
    public User getParticipantTwo() { return participantTwo; }
    public Instant getCreatedAt() { return createdAt; }

    public boolean contains(User user) {
        return participantOne.getId().equals(user.getId()) || participantTwo.getId().equals(user.getId());
    }

    public User otherParticipant(User user) {
        return participantOne.getId().equals(user.getId()) ? participantTwo : participantOne;
    }
}
