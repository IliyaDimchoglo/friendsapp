package com.skysoft.business.api.service.impl;

import com.skysoft.business.api.model.AccessRequestEntity;
import com.skysoft.business.api.model.AccountEntity;
import com.skysoft.business.api.repository.AccessRepository;
import com.skysoft.business.api.service.AccessDBService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public Optional<AccessRequestEntity> findByEmailAndConfirmationCode(String email, String confirmationCode) {
        return accessRepository.findFirstByEmailAndConfirmationCode(email, confirmationCode);
    }

    @Override
    public void confirmAccessRequest(AccessRequestEntity accessRequest, AccountEntity accountEntity) {
        log.info("[x] Confirm access request with id: {}.", accessRequest.getId());
        accessRequest.setAccount(accountEntity);
        accessRepository.save(accessRequest);
    }
}
