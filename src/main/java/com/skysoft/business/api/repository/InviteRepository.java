package com.skysoft.business.api.repository;

import com.skysoft.business.api.model.InviteEntity;
import com.skysoft.business.api.model.InviteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<InviteEntity, UUID> {
    List<InviteEntity> findAllByAccountIdAndInviteStatus(UUID id, InviteStatus inviteStatus);
    Optional<InviteEntity> findFirstByAccountIdAndInviteStatus(UUID id, InviteStatus inviteStatus);
    Optional<InviteEntity> findFirstByAccountIdAndFriendIdAndInviteStatus(UUID id, UUID friendId, InviteStatus inviteStatus);
    boolean existsByAccountIdAndFriendIdAndInviteStatus(UUID accountId, UUID friendId, InviteStatus inviteStatus);
}
