package com.skysoft.repository;

import com.skysoft.model.AccessRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccessRepository extends JpaRepository<AccessRequestEntity, UUID> {

    Optional<AccessRequestEntity> findFirstByIdAndConfirmationCode(UUID id, UUID confirmationCode);

    Optional<AccessRequestEntity> findFirstByUsername(String username);

    boolean existsByEmailAndUsernameAndConfirmedTrue(String email, String username);

}
