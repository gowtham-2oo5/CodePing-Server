package com.codeping.server.coreserver.repos;

import com.codeping.server.coreserver.models.CPUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CPUsersRepository extends JpaRepository<CPUsers, UUID> {
    Optional<CPUsers> findByEmail(String email);

    Optional<CPUsers> findByFirebaseUid(String firebaseUid);
}