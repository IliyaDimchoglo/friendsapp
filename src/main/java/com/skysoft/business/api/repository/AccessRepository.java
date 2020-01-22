package com.skysoft.business.api.repository;

import com.skysoft.business.api.model.AccessRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccessRepository extends JpaRepository<AccessRequestEntity, UUID> {

    Optional<AccessRequestEntity> findFirstByConfirmationCode(String confirmationCode);

    Optional<AccessRequestEntity> findFirstByUsername(String username);


}
