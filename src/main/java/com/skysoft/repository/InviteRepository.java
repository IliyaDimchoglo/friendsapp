package com.skysoft.repository;

import com.skysoft.model.InviteEntity;
import com.skysoft.model.InviteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InviteRepository extends JpaRepository<InviteEntity, UUID> {

    List<InviteEntity> findAllByAccount2_UsernameAndInviteStatus(String username, InviteStatus inviteStatus);

    List<InviteEntity> findAllByAccount1_UsernameAndInviteStatus(String username, InviteStatus inviteStatus);

    Optional<InviteEntity> findFirstByAccount1_UsernameAndAccount2_UsernameAndInviteStatus(
            String username, String friendName, InviteStatus status);

    boolean existsByAccount1_UsernameAndAccount2_UsernameAndInviteStatusOrAccount2_UsernameAndAccount1_UsernameAndInviteStatus(String username, String friendName, InviteStatus inviteStatus, String name, String friend, InviteStatus status);
}

