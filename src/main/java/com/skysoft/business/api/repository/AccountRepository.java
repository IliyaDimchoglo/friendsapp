package com.skysoft.business.api.repository;

import com.skysoft.business.api.model.AccountEntity;
import com.skysoft.business.api.model.FriendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
    Optional<AccountEntity> findFirstByUsername(String username);
    boolean existsByUsernameOrEmail(String username, String email);
    List<AccountEntity> findAllByIdIn(List<UUID> ids);
}
