package com.skysoft.repository;

import com.skysoft.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
    Optional<AccountEntity> findFirstByUsername(String username);
    boolean existsByUsernameOrEmail(String username, String email);
    List<AccountEntity> findAllByIdIsNotIn(List<UUID> ids);
}
