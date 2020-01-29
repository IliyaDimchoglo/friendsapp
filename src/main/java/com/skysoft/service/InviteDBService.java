package com.skysoft.service;

import com.skysoft.exception.NotFoundException;
import com.skysoft.model.InviteEntity;
import com.skysoft.model.InviteStatus;

import java.util.List;
import java.util.Optional;

public interface InviteDBService {

    List<InviteEntity> getPendingInvitesByUsername(String username);

    List<InviteEntity> getPendingInvitesByFriendName(String username);

    boolean existPendingInvite(String username, String friendName);

    Optional<InviteEntity> findByUsernameAndFriendNameAndStatus(String username, String friendName);

    default InviteEntity getInviteByUsernameAndFriendNameInStatusPending(String username, String friendName) throws NotFoundException {
        return findByUsernameAndFriendNameAndStatus(username, friendName).orElseThrow(() -> new NotFoundException("Invite not found"));
    }

    void save(InviteEntity entity);
}
