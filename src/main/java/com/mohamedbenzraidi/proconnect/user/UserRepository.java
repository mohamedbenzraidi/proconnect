package com.mohamedbenzraidi.proconnect.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByEmail(String email);
    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmail(String email);
    // Recherche par prénom, nom ou email en excluant l'utilisateur connecté
    @Query("SELECT u FROM User u WHERE u.id != :excludedId AND (" +
            "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<User> searchUsersExcludingSelf(@Param("query") String query, @Param("excludedId") UUID excludedId);
}