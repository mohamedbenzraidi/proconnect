package com.mohamedbenzraidi.proconnect.connection;

import com.mohamedbenzraidi.proconnect.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, UUID> {

    // Récupère toutes les connexions concernant l'utilisateur connecté
    @Query("SELECT c FROM Connection c WHERE " +
            "(c.sender = :user OR c.receiver = :user) AND c.sender.id <> c.receiver.id")
    List<Connection> findValidConnectionsForUser(@Param("user") User user);

    boolean existsBySenderAndReceiver(User sender, User receiver);
}