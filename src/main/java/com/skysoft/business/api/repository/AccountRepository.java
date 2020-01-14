package com.skysoft.business.api.repository;

import com.skysoft.business.api.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
    Optional<AccountEntity> findFirstByUsername(String username);
    boolean existsByUsernameAndEmail(String username, String email);
    Optional<AccountEntity> findFirstById(UUID id);

}
