package com.skysoft.business.api.repository;

import com.skysoft.business.api.model.FriendEntity;
import com.skysoft.business.api.model.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendRepository extends JpaRepository<FriendEntity, UUID> {
    List<FriendEntity> findAllByUserId(UUID id);
    Optional<FriendEntity> findFirstByUserIdAndAccountEntity_IdAndStatus(UUID id, UUID friendId, FriendStatus friendStatus);
}
