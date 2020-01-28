package com.skysoft.repository;

import com.skysoft.model.InviteEntity;
import com.skysoft.model.InviteStatus;
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

    boolean existsByAccount_UsernameAndFriend_UsernameAndInviteStatusOrFriend_UsernameAndAccount_UsernameAndInviteStatus(String username, String friendName, InviteStatus inviteStatus, String name, String friend, InviteStatus status);
}

