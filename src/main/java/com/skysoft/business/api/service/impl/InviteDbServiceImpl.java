package com.skysoft.business.api.service.impl;

import com.skysoft.business.api.exception.BadRequestException;
import com.skysoft.business.api.exception.NotFoundException;
import com.skysoft.business.api.model.InviteEntity;
import com.skysoft.business.api.model.InviteStatus;
import com.skysoft.business.api.repository.InviteRepository;
import com.skysoft.business.api.service.InviteDBService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.skysoft.business.api.model.InviteStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class InviteDbServiceImpl implements InviteDBService {

    private final InviteRepository inviteRepository;

    @Override
    public void resetInviteRequest(UUID id, UUID friendId) {
            InviteEntity inviteEntity = inviteRepository.findFirstByAccountIdAndFriendIdAndInviteStatus(id, friendId, ACCEPT)
                    .orElseThrow(() -> new NotFoundException("Invite not found."));
            inviteEntity.setInviteStatus(REJECT);
            save(inviteEntity);
            log.info("[x] Invite request reset.");
    }

    @Override
    public List<InviteEntity> findAll(UUID id, InviteStatus inviteStatus) {
        return inviteRepository.findAllByAccountIdAndInviteStatus(id, inviteStatus);
    }

    @Override
    public Optional<InviteEntity> getOptionalByAccountIdAndStatus(UUID accountId, InviteStatus inviteStatus) {
        return inviteRepository.findFirstByAccountIdAndInviteStatus(accountId, inviteStatus);
    }

    @Override
    public boolean existsInvite(UUID accountId, UUID friendId) {
        if (inviteRepository.existsByAccountIdAndFriendIdAndInviteStatus(accountId, friendId, PENDING)) {
            return true;
        } else
            return inviteRepository.existsByAccountIdAndFriendIdAndInviteStatus(accountId, friendId, ACCEPT);
    }

    @Override
    public void save(InviteEntity entity) {
        try {
            inviteRepository.save(entity);
            log.info("[x] Invite was successful saved.");
        }catch (Exception e){
            log.warn("[x] Failed save invite with message: {}",e.getMessage());
            throw new BadRequestException("Failed save invite.");
        }
    }

}
