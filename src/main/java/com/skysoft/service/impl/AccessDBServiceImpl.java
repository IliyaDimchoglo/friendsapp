package com.skysoft.service.impl;

import com.skysoft.model.AccessRequestEntity;
import com.skysoft.model.AccountEntity;
import com.skysoft.repository.AccessRepository;
import com.skysoft.service.AccessDBService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessDBServiceImpl implements AccessDBService {

    private final AccessRepository accessRepository;

    @Override
    public AccessRequestEntity save(AccessRequestEntity entity) {
        return accessRepository.save(entity);
    }

    @Override
    public Optional<AccessRequestEntity> getOptionalByUsername(String username) {
        return accessRepository.findFirstByUsername(username);
    }

    @Override
    public Optional<AccessRequestEntity> findOptionalByIdAndConfirmationCode(UUID accessId, UUID confirmationCode) {
        return accessRepository.findFirstByIdAndConfirmationCode(accessId, confirmationCode);
    }

    @Override
    public boolean existConfirmedEmailAndUsername(String email, String username) {
        return accessRepository.existsByEmailAndUsernameAndConfirmedTrue(email, username);
    }

    @Override
    public void confirmAccessRequest(AccessRequestEntity accessRequest, AccountEntity accountEntity) {
        log.info("[x] Confirm access request with id: {}.", accessRequest.getId());
        accessRequest.setAccount(accountEntity);
        accessRepository.save(accessRequest);
    }
}
