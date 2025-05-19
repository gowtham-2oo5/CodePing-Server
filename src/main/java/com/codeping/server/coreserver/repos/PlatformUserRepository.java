package com.codeping.server.coreserver.repos;

import com.codeping.server.coreserver.models.PlatformUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlatformUserRepository extends JpaRepository<PlatformUser, Long> {
    Optional<PlatformUser> findByUserIdAndPlatform(UUID userId, String platform);

    List<PlatformUser> findByUserId(UUID userId);

    @Query("SELECT DISTINCT pu.username FROM PlatformUser pu WHERE pu.username IS NOT NULL")
    List<String> findAllUsernames();
}