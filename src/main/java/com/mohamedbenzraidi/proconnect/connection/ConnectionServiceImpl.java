package com.mohamedbenzraidi.proconnect.connection;

import com.mohamedbenzraidi.proconnect.connection.dto.ConnectionDto;
import com.mohamedbenzraidi.proconnect.connection.dto.UserSearchDto;
import com.mohamedbenzraidi.proconnect.user.User;
import com.mohamedbenzraidi.proconnect.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ConnectionServiceImpl implements ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final UserRepository userRepository;

    public ConnectionServiceImpl(ConnectionRepository connectionRepository, UserRepository userRepository) {
        this.connectionRepository = connectionRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSearchDto> searchUsers(String query, String currentLoggedInEmail) {
        User currentUser = userRepository.findByEmailIgnoreCase(currentLoggedInEmail)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        // Recherche en SQL direct avec exclusion de l'ID courant
        Map<UUID, ConnectionStatus> connectionStatuses = connectionRepository
                .findValidConnectionsForUser(currentUser)
                .stream()
                .collect(Collectors.toMap(
                        connection -> connection.getSender().getId().equals(currentUser.getId())
                                ? connection.getReceiver().getId()
                                : connection.getSender().getId(),
                        Connection::getStatus,
                        (existingStatus, duplicateStatus) -> existingStatus
                ));

        return userRepository.searchUsersExcludingSelf(query, currentUser.getId())
                .stream()
                .map(user -> UserSearchDto.fromEntity(
                        user,
                        connectionStatuses.get(user.getId()) == null
                                ? null
                                : connectionStatuses.get(user.getId()).name()
                ))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConnectionDto> getConnectionsForUser(String currentLoggedInEmail) {
        User currentUser = userRepository.findByEmailIgnoreCase(currentLoggedInEmail)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

        // Récupération stricte des connexions impliquant l'utilisateur courant
        List<Connection> connections = connectionRepository.findValidConnectionsForUser(currentUser);

        return connections.stream()
                .map(ConnectionDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public ConnectionDto sendRequest(String senderEmail, UUID receiverId) {
        User sender = userRepository.findByEmailIgnoreCase(senderEmail)
                .orElseThrow(() -> new IllegalArgumentException("Expéditeur introuvable"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Destinataire introuvable"));

        if (sender.getId().equals(receiverId)) {
            throw new IllegalArgumentException("Vous ne pouvez pas vous envoyer une demande à vous-même");
        }

        boolean alreadyExists = connectionRepository.existsBySenderAndReceiver(sender, receiver)
                || connectionRepository.existsBySenderAndReceiver(receiver, sender);
        if (alreadyExists) {
            throw new IllegalStateException("Une connexion ou demande existe déjà entre ces utilisateurs");
        }

        Connection connection = new Connection(sender, receiver, ConnectionStatus.PENDING);
        Connection saved = connectionRepository.save(connection);

        return ConnectionDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public ConnectionDto acceptRequest(UUID connectionId) {
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new IllegalArgumentException("Demande de connexion introuvable"));

        connection.setStatus(ConnectionStatus.ACCEPTED);
        Connection updated = connectionRepository.save(connection);

        return ConnectionDto.fromEntity(updated);
    }

    @Override
    @Transactional
    public void removeConnection(UUID connectionId) {
        if (!connectionRepository.existsById(connectionId)) {
            throw new IllegalArgumentException("Connexion introuvable");
        }
        connectionRepository.deleteById(connectionId);
    }
}