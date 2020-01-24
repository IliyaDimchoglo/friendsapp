package com.skysoft.business.api.repository;

import com.skysoft.business.api.model.InviteEntity;
import com.skysoft.business.api.model.InviteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<InviteEntity, UUID> {


    List<InviteEntity> findAllByFriendUsernameAndInviteStatus(String username, InviteStatus inviteStatus);

    List<InviteEntity> findAllByAccountUsernameAndInviteStatus(String username, InviteStatus inviteStatus);

    Optional<InviteEntity> findFirstByAccountUsernameAndInviteStatus(String username, InviteStatus inviteStatus);

    Optional<InviteEntity> findFirstByAccountUsernameAndFriendUsernameAndInviteStatus(
            String username, String friendName, InviteStatus status);// TODO: 23.01.20 implement in send invite reverse params(friend -> account)

    boolean existsByAccount_UsernameAndFriend_UsernameAndInviteStatus(String username, String friendName, InviteStatus inviteStatus);
}

