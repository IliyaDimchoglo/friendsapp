package com.skysoft.service.impl;

import com.skysoft.model.InviteEntity;
import com.skysoft.model.InviteStatus;
import com.skysoft.repository.InviteRepository;
import com.skysoft.service.InviteDBService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.skysoft.model.InviteStatus.PENDING;

@Slf4j
@Service
@RequiredArgsConstructor
public class InviteDbServiceImpl implements InviteDBService {

    private final InviteRepository inviteRepository;

    @Override
    public List<InviteEntity> findAllInvitesByFriendUsernameAndStatus(String username, InviteStatus inviteStatus) {
        return inviteRepository.findAllByFriendUsernameAndInviteStatus(username, inviteStatus);
    }

    @Override
    public List<InviteEntity> findAllInvitesByAccountUsernameAndStatus(String username, InviteStatus inviteStatus) {
        return inviteRepository.findAllByAccountUsernameAndInviteStatus(username, inviteStatus);
    }

    @Override
    public Optional<InviteEntity> getOptionalByUsernameAndFriendNameAndStatus(String username, String friendName) {
        return inviteRepository.findFirstByAccountUsernameAndFriendUsernameAndInviteStatus(username, friendName, PENDING);
    }

    @Override
    public boolean existPendingInvite(String username, String friendName, InviteStatus status) {
        return inviteRepository.existsByAccount_UsernameAndFriend_UsernameAndInviteStatusOrFriend_UsernameAndAccount_UsernameAndInviteStatus(
                username, friendName, status, username, friendName, status);

    }

    @Override
    public void save(InviteEntity entity) {
        inviteRepository.save(entity);
        log.info("[x] Invite was successful saved.");
    }
}
