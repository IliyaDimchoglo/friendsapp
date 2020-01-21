package com.skysoft.business.api.repository;

import com.skysoft.business.api.model.InviteEntity;
import com.skysoft.business.api.model.InviteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<InviteEntity, UUID> {

    List<InviteEntity> findAllByFriend_UsernameAndInviteStatus(String username, InviteStatus inviteStatus);

    List<InviteEntity> findAllByAccount_UsernameAndInviteStatus(String username, InviteStatus inviteStatus);

    Optional<InviteEntity> findFirstByAccount_UsernameAndInviteStatus(String username, InviteStatus inviteStatus);

    InviteEntity findFirstByAccount_UsernameAndFriend_UsernameOrFriend_UsernameAndAccount_Username(
            String username, String friendName, String friendUsername, String accountName);
}
