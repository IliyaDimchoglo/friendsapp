package com.skysoft.business.api.service;

import com.skysoft.business.api.exception.NotFoundException;
import com.skysoft.business.api.model.InviteEntity;
import com.skysoft.business.api.model.InviteStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InviteDBService {

    void resetInviteRequest(UUID id, UUID friendId);

    List<InviteEntity> findAll(UUID id, InviteStatus inviteStatus);

    boolean existsInvite(UUID accountId, UUID friendId);

    Optional<InviteEntity> getOptionalByAccountIdAndStatus(UUID accountId, InviteStatus inviteStatus);

    default InviteEntity getInviteEntityByAccountIdAndStatus(UUID accountId, InviteStatus inviteStatus){
        return  getOptionalByAccountIdAndStatus(accountId, inviteStatus).orElseThrow(
                () -> new NotFoundException("Invite Entity not found"));
    }

    void save(InviteEntity entity);
}
