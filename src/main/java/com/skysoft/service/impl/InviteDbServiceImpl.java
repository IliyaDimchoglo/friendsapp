package com.skysoft.service.impl;

import com.skysoft.model.InviteEntity;
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
    public List<InviteEntity> getPendingInvitesByUsername(String username) {
        return inviteRepository.findAllByAccount2_UsernameAndInviteStatus(username, PENDING);
    }

    @Override
    public List<InviteEntity> getPendingInvitesByFriendName(String username) {
        return inviteRepository.findAllByAccount1_UsernameAndInviteStatus(username, PENDING);
    }

    @Override
    public Optional<InviteEntity> findByUsernameAndFriendNameAndStatus(String username, String friendName) {
        return inviteRepository.findFirstByAccount1_UsernameAndAccount2_UsernameAndInviteStatus(username, friendName, PENDING);
    }

    @Override
    public boolean existPendingInvite(String username, String friendName) {
        return inviteRepository.existsByAccount1_UsernameAndAccount2_UsernameAndInviteStatusOrAccount2_UsernameAndAccount1_UsernameAndInviteStatus(
                username, friendName, PENDING, username, friendName, PENDING);

    }

    @Override
    public void save(InviteEntity entity) {
        inviteRepository.save(entity);
        log.info("[x] Invite was successful saved.");
    }
}
