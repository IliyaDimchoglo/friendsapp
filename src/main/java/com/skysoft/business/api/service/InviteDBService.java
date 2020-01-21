package com.skysoft.business.api.service;

import com.skysoft.business.api.exception.NotFoundException;
import com.skysoft.business.api.model.InviteEntity;
import com.skysoft.business.api.model.InviteStatus;

import java.util.List;
import java.util.Optional;

public interface InviteDBService {

    List<InviteEntity> findAllInvitesByAccountUsernameAndStatus(String username, InviteStatus inviteStatus);

    InviteEntity findInvite(String username, String friendName);

    Optional<InviteEntity> getOptionalByAccountUsernameAndStatus(String username, InviteStatus inviteStatus);

    default InviteEntity getInviteEntityByAccount_UsernameAndStatus(String username, InviteStatus inviteStatus){
        return  getOptionalByAccountUsernameAndStatus(username, inviteStatus).orElseThrow(
                () -> new NotFoundException("Invite not found"));
    }

    void save(InviteEntity entity);
}
