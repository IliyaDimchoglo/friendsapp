package com.skysoft.business.api.service.impl;

import com.skysoft.business.api.model.InviteEntity;
import com.skysoft.business.api.model.InviteStatus;
import com.skysoft.business.api.repository.InviteRepository;
import com.skysoft.business.api.service.InviteDBService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InviteDbServiceImpl implements InviteDBService {

    private final InviteRepository inviteRepository;

    @Override
    public List<InviteEntity> findAllInvitesByFriendUsernameAndStatus(String username, InviteStatus inviteStatus) {
        return inviteRepository.findAllByFriend_UsernameAndInviteStatus(username, inviteStatus);
    }

    @Override
    public List<InviteEntity> findAllInvitesByAccountUsernameAndStatus(String username, InviteStatus inviteStatus) {
        return inviteRepository.findAllByAccount_UsernameAndInviteStatus(username, inviteStatus);
    }

    @Override
    public Optional<InviteEntity> getOptionalByAccountUsernameAndStatus(String username, InviteStatus inviteStatus) {
        return inviteRepository.findFirstByAccount_UsernameAndInviteStatus(username, inviteStatus);
    }

    @Override
    public InviteEntity findInvite(String username, String friendName) {
        return inviteRepository.findFirstByAccount_UsernameAndFriend_UsernameOrFriend_UsernameAndAccount_Username(
                username, friendName, friendName, username);
    }

    @Override
    public void save(InviteEntity entity) {
        inviteRepository.save(entity);
        log.info("[x] Invite was successful saved.");
    }
}
