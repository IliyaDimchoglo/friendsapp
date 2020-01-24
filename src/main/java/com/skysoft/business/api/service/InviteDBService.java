package com.skysoft.business.api.service;

import com.skysoft.business.api.exception.NotFoundException;
import com.skysoft.business.api.model.InviteEntity;
import com.skysoft.business.api.model.InviteStatus;

import java.util.List;
import java.util.Optional;

public interface InviteDBService {

    List<InviteEntity> findAllInvitesByFriendUsernameAndStatus(String username, InviteStatus inviteStatus);

    List<InviteEntity> findAllInvitesByAccountUsernameAndStatus(String username, InviteStatus inviteStatus);

    boolean existPendingInvite(String username, String friendName, InviteStatus status);

    Optional<InviteEntity> getOptionalByUsernameAndFriendNameAndStatus(String username, String friendName);

    default InviteEntity getInviteByUsernameAndFriendNameAndStatusPending(String username, String friendName) throws NotFoundException {
        return getOptionalByUsernameAndFriendNameAndStatus(username, friendName).orElseThrow(() -> new NotFoundException("Invite not found"));
    }

    void save(InviteEntity entity);
}
